package utils;

import java.util.Vector;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderTest1 {
	
	@Test(dataProvider = "testdb")
    public void parameterIntTest(Class clzz, String str,String index) {
       System.out.println("Parameterized Number is : " + clzz);
       System.out.println("Parameterized Number is : " + str);
    }

    //This function will provide the patameter data
    @DataProvider(name = "testdb")
    public Object[][] parameterIntTestProvider() {
    	//二维数组
        return new Object[][]{
                   {Vector.class, "test1","1"},
                   {String.class, "test2","1"},
                   {Integer.class, "test3","1"}
                  };
    }

}
