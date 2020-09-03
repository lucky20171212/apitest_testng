package apitest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.checkpoint.CheckPointUtils;
import com.github.checkpoint.JsonCheckResult;
import com.github.crab2died.ExcelUtils;

import utils.DbCheckUtils;
import utils.ExcelToMapUtils;
import utils.HttpClientUtils;
import utils.ParamUtils;

public class ApiTest {
	
	public static void main(String[] args) {
		String path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"apitest8.xlsx";
		 XmlMapper xmlMapper = new XmlMapper();
	     try {
	    	HttpClientUtils.openProxy =true;
	    	//读取参数 参数任意变化
	    	List<Map<String, Object>> paramsList = ExcelToMapUtils.importExcel(path, 1);
	    	
	    	List<TestCaseResult> testCaseResults =new ArrayList<TestCaseResult>();
	    	//循环套循环
	    	//1000组 并行
	    	for (Map<String, Object> map : paramsList) {
	    		//从excel 获取
	    		ParamUtils.addFromMap(map);
	    		List<TestCase> list = ExcelUtils.getInstance().readExcel2Objects(path, TestCase.class);
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
    				replace(testCase);
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
    				
    				addCorrelation(result,testCase);
    				checkPoint(result,testCase,caseResult);
    			
    				//caseResult.setCaseName(testCase.getCaseName());
    				//...
    				BeanUtils.copyProperties(caseResult, testCase);
    				testCaseResults.add(caseResult);
    				System.out.println("add "+caseResult);
    				System.out.println("all"+testCaseResults);
    			}
    			ParamUtils.clear();
			}
	    	
	    	//结果汇总
	    	System.out.println(testCaseResults);
	    	//用哪个对象
	    	String result_path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"result_"+System.currentTimeMillis()+".xlsx";
	    	ExcelUtils.getInstance().exportObjects2Excel(testCaseResults, TestCaseResult.class,result_path);
			//System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void checkPoint(String result, TestCase testCase,TestCaseResult caseResult) {
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
	
	private static void addCorrelation(String result, TestCase testCase) {
		if(StringUtils.isNotBlank(testCase.getCorrelationJson())) {
			ParamUtils.addFromJson(result, testCase.getCorrelationJson());
			ParamUtils.println();
		}
		if(StringUtils.isNotBlank(testCase.getCorrelationText())) {
			ParamUtils.addFromText(result, testCase.getCorrelationText());
		}
	}
	
//	static String pattern = "\\$\\{(.*?)\\}";
//	private static void replace(TestCase testcase,Map<String, Object> map) {
//		System.out.println(testcase.getUrl());
//		//url
//		String url=testcase.getUrl();
//		if(StringUtils.isNoneBlank(url)) {
//			 Pattern r2 = Pattern.compile(pattern);
//			    Matcher m2= r2.matcher(url);
//			    while(m2.find()) {
//			    	String map_key=m2.group(1);
//			    	url =url.replace(m2.group(), StringToMapUtils.getFromMap(map, map_key));
//			    }
//			    testcase.setUrl(url);
//		}
//		//body
//		String body = testcase.getParams();
//		if(StringUtils.isNoneBlank(body)) {
//			 Pattern r2 = Pattern.compile(pattern);
//			    Matcher m2= r2.matcher(body);
//			    while(m2.find()) {
//			    	String map_key=m2.group(1);
//			    	body =body.replace(m2.group(), StringToMapUtils.getFromMap(map, map_key));
//			    }
//			    testcase.setParams(body);
//		}
//		
//		//header
//		String header = testcase.getHeader();
//		if(StringUtils.isNoneBlank(header)) {
//			 Pattern r2 = Pattern.compile(pattern);
//			    Matcher m2= r2.matcher(header);
//			    while(m2.find()) {
//			    	String map_key=m2.group(1);
//			    	header =header.replace(m2.group(), StringToMapUtils.getFromMap(map, map_key));
//			    }
//			    testcase.setHeader(header);
//		}
//		
//		//可以再优化
//	}
	
	
	//优化结果 fiddler 抓包
//	private static void replace(TestCase testcase,Map<String, Object> map) {
//		//url
//		String url=testcase.getUrl();
//		if(StringUtils.isNoneBlank(url)) {
//			testcase.setUrl(replace(url,map));
//		}
//		//body
//		String body = testcase.getParams();
//		if(StringUtils.isNoneBlank(body)) {
//			testcase.setParams(replace(body,map));
//		}
//		
//		//header
//		String header = testcase.getHeader();
//		if(StringUtils.isNoneBlank(header)) {
//			testcase.setHeader(replace(header,map));
//		}
//	}
	
	private static void replace(TestCase testcase) {
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
