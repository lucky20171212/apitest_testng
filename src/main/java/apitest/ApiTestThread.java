package apitest;

import java.io.File;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.checkpoint.CheckPointUtils;
import com.github.checkpoint.JsonCheckResult;
import com.github.crab2died.ExcelUtils;

import utils.DbCheckUtils;
import utils.EmailUtils;
import utils.ExcelToMapUtils;
import utils.HttpClientUtils;
import utils.ParamUtils;


//线程就是个对象
class TestCaseTask extends Thread{
	
	private Map<String, Object> map;
	
	private XmlMapper xmlMapper = new XmlMapper();
	
	private CountDownLatch countDownLath;
	

	public TestCaseTask(Map<String, Object> map, CountDownLatch countDownLath) {
		super();
		this.map = map;
		this.countDownLath = countDownLath;
	}


	@Override
	public void run() {
		
		List<TestCaseResult> allCaseResults=new ArrayList<TestCaseResult>();
		try {
		System.out.println("start--"+Thread.currentThread().getName());
		//从excel 获取
		ParamUtils.addFromMap(map);
		List<TestCase> list = ExcelUtils.getInstance().readExcel2Objects(ApiTestThread.path, TestCase.class);
		//过滤 去重
		List<TestCase> filterList= list.stream().filter(testCase->"是".equals(testCase.getIsOpen())).collect(Collectors.toList());
		//排序
		Collections.sort(filterList, (o1,o2)->o1.getOrder()-o2.getOrder());
		String result="";
		//100接口  80 90 
		for (TestCase testCase : filterList) {
			TestCaseResult caseResult=new TestCaseResult();
			System.out.println(testCase);
			//replace(testCase,map);
			ApiTestThread.replace(testCase);
			if("get".equals(testCase.getType())) {
				result = HttpClientUtils.doGet(testCase.getUrl(),testCase.getHeader());
			}else if ("post".equals(testCase.getType())) {
				result =HttpClientUtils.doPost(testCase.getUrl(), testCase.getParams(),testCase.getHeader());
			}else if("postjson".equals(testCase.getType())) {
				result=HttpClientUtils.doPostJson(testCase.getUrl(), testCase.getParams(),testCase.getHeader());
			}else if ("getXml".equalsIgnoreCase(testCase.getType())) {
				result = HttpClientUtils.doGet(testCase.getUrl(),testCase.getHeader());
				//xml
				Map mapxml =xmlMapper.readValue(result, HashMap.class);
				result =JSON.toJSONString(mapxml);
				System.out.println("xml "+result);
			}
		   // ParamUtils.addCorrelationParams(result, testCase.getCorrelationJson(), testCase.getCorrelationText());
			
			ApiTestThread.addCorrelation(result,testCase);
			ApiTestThread.checkPoint(result,testCase,caseResult);
		
			//caseResult.setCaseName(testCase.getCaseName());
			//...
			BeanUtils.copyProperties(caseResult, testCase);
			allCaseResults.add(caseResult);
		}
		} catch (Exception e) {
		}finally {
			ApiTestThread.testCaseResults.addAll(allCaseResults);
			ParamUtils.clear();
			allCaseResults.clear();
			countDownLath.countDown();
		}
	}
	
}

public class ApiTestThread {
	
	public static List<TestCaseResult> testCaseResults =new ArrayList<TestCaseResult>();
	public static final String path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"apitest9.xlsx";
	
	private static final Logger logger = LoggerFactory.getLogger(ApiTestThread.class);
	
	public static void main(String[] args) {
		
		  System.out.println("main 参数");
		  logger.info("main");
	      for (String arg : args) {
		   System.out.println("arg----"+arg);
	      }
	     try {
	    	 if(args.length>=1) {
	    		String isOpenProxy= args[0];
	    		if("true".equals(isOpenProxy)) {
	    			HttpClientUtils.openProxy =true;
	    		}
	    	}
	    	//读取参数 参数任意变化 1万数据
	    	List<Map<String, Object>> paramsList = ExcelToMapUtils.importExcel(path, 1);
	    	
	    	//循环套循环
	    	//1000组 并行  
	    	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(200);
	    	CountDownLatch countDownLath  =new CountDownLatch(paramsList.size());
	    	for (Map<String, Object> map : paramsList) {
	    		fixedThreadPool.execute(new TestCaseTask(map,countDownLath));
	    		//new TestCaseTask(map,countDownLath).start();
			}
	    	
	    	countDownLath.await();
	    	//结果汇总
	    	System.out.println("结果"+testCaseResults.size());//日志容易丢
	    	//用哪个对象
	    	String result_path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"result_"+System.currentTimeMillis()+".xlsx";
	    	ExcelUtils.getInstance().exportObjects2Excel(testCaseResults, TestCaseResult.class,result_path);
	    	//result_path 发送邮箱
			//System.out.println(list);
	    	EmailUtils.sendEmailsWithAttachments("测试结果", "请查收", result_path);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("出错了{}",e);
		}
	}
	
	public static void checkPoint(String result, TestCase testCase,TestCaseResult caseResult) {
		String checkPoint = ParamUtils.replace(testCase.getCheckpoint());
		//检查点
		JsonCheckResult jsonCheckResult=CheckPointUtils.check(result, checkPoint);
		System.out.println("返回结果"+result);
		System.out.println(checkPoint+ "自定义检查点来了"+jsonCheckResult.isResult()+"  "+jsonCheckResult.getMsg());
		caseResult.setApiCheckResult(jsonCheckResult.getMsg());
		String dbcheck = ParamUtils.replace(testCase.getDbCheckpoint());
		String dbcheckResult= DbCheckUtils.check(dbcheck);
		caseResult.setDbCheckResult(dbcheckResult);
		//1  1.0
		System.out.println("数据库检查点"+dbcheck);
		
		testCase.setCheckpoint(checkPoint);
		testCase.setDbCheckpoint(dbcheck);
		
	}
	
	public static void addCorrelation(String result, TestCase testCase) {
		if(StringUtils.isNotBlank(testCase.getCorrelationJson())) {
			ParamUtils.addFromJson(result, testCase.getCorrelationJson());
			ParamUtils.println();
		}
		if(StringUtils.isNotBlank(testCase.getCorrelationText())) {
			ParamUtils.addFromText(result, testCase.getCorrelationText());
		}
	}

	
	public static void replace(TestCase testcase) {
		//url
		String url=testcase.getUrl();
		if(StringUtils.isNoneBlank(url)) {
			testcase.setUrl(ParamUtils.replace(url));
		}
		//body
		String body = testcase.getParams();
		if(StringUtils.isNoneBlank(body)) {
			testcase.setParams(ParamUtils.replace(body));
		}
		
		//header
		String header = testcase.getHeader();
		if(StringUtils.isNoneBlank(header)) {
			testcase.setHeader(ParamUtils.replace(header));
		}
		
		
	}
	
	
//	private static String replace(String str,Map<String, Object> map) {
//		   Pattern r2 = Pattern.compile(pattern);
//		    Matcher m2= r2.matcher(str);
//		    while(m2.find()) {
//		    	String map_key=m2.group(1);
//		    	str =str.replace(m2.group(), StringToMapUtils.getFromMap(map, map_key));
//		    }
//		  return str;
//	}

}
