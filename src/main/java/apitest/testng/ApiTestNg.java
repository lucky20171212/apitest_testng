package apitest.testng;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.ctc.wstx.osgi.ValidationSchemaFactoryProviderImpl;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.crab2died.ExcelUtils;

import apitest.ApiTestThread;
import apitest.TestCase;
import apitest.TestCaseResult;
import utils.EmailUtils;
import utils.ExcelToMapUtils;
import utils.HttpClientUtils;
import utils.ParamUtils;

public class ApiTestNg {
	
	private XmlMapper xmlMapper = new XmlMapper();
	private static final Logger logger = LoggerFactory.getLogger(ApiTestNg.class);
	
	
	//等待
	@AfterClass
	public void AfterTest() {
		try {
		logger.info("结果"+ApiTestThread.testCaseResults.size());
    	//用哪个对象
    	String result_path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"result_"+System.currentTimeMillis()+".xlsx";
    	ExcelUtils.getInstance().exportObjects2Excel(ApiTestThread.testCaseResults, TestCaseResult.class,result_path);
    	//result_path 发送邮箱
		//System.out.println(list);
    	EmailUtils.sendEmailsWithAttachments("测试结果", "请查收", result_path);
		}catch (Exception e) {
			logger.error(" 测试结果 {}",e);
		}finally {
			ApiTestThread.testCaseResults.clear();
		}
	}
	
	@Test(dataProvider = "getExcel")
    public void testCase(Map<String, Object> map) {
		logger.info(Thread.currentThread().getName());
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
		}
    }
	
	
	//数据准备，还支持并行
	@DataProvider(name = "getExcel",parallel = true)
    public Iterator<Object[]> getExcelProvider() {
    	String path = System.getProperty("user.dir") + File.separator + "data" + File.separator + "apitest9.xlsx";
    	//exel测试数据源
    	List<Map<String, Object>> listMaps = ExcelToMapUtils.importExcel(path, 1);
    	
    	//testng  	List<Object[]>  list<Object[0]>
    	List<Object[]> dataProvider = new ArrayList<Object[]>();
    	listMaps.forEach(d->dataProvider.add(new Object[] {d}));
        return dataProvider.iterator();
    }
    

}
