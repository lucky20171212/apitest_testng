package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import com.github.checkpoint.CheckPointUtils;
import com.github.checkpoint.JsonCheckResult;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import utils.HttpClientUtils;

@Epic("Allure Epic")
@Feature("Allure Feature")
public class MyHttpClientUtilsTest {
	//断言
	private Assertion assertion;
	
  @BeforeClass(description = "测试准备")
  public void beforeClass() {
	  HttpClientUtils.openProxy=true;
	  assertion = new Assertion();
  }

  @AfterClass
  public void afterClass() {
  }


  
  @Test
  @Story("failedTest")
  @Description("错误测试")
  public void failedTest(){
      Assert.assertEquals(2,3);
  }
  
  @Story("短信发送Story")
  @Description("描述发送短信接口")
  @Issue("123")
  @TmsLink("bug-1213")
  @Test(dataProvider = "mytest",threadPoolSize = 10)
  public void doGetTest(String loginname,String loginpass) {
	  System.out.println(Thread.currentThread().getName());
	  String testurl = "http://118.24.13.38:8080/goods/UserServlet?method=loginMobile&loginname="+loginname+"&loginpass="+loginpass+"";
	  String result = HttpClientUtils.doGet(testurl);
      System.out.println(result);
      JsonCheckResult checkResult = CheckPointUtils.check(result, "msg=登录成功");
      System.out.println(checkResult.getMsg());
      assertion.assertEquals(checkResult.isResult(), true);
  }
  
  @DataProvider(name = "mytest", parallel = true)
  public Iterator<Object[]> parameterIntTestProvider() {
  	List<Object[]> dataProvider = new ArrayList<Object[]>();
  	dataProvider.add(new Object[] {"abc","abc"});
  	dataProvider.add(new Object[] {"test1","test1"});
  	dataProvider.add(new Object[] {"test1121","test11212"});
    return dataProvider.iterator();
  }
  
}
  
