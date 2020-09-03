package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderTest2 {
	
	//list<Object[0]>  ==Object[][0]
	@Test(dataProvider = "mytest")
    public void parameterIntTest(TestBean  bean) {
       System.out.println("Parameterized Number is : " + bean);
       System.out.println("Parameterized Number is : " + bean);
    }

	
	    @DataProvider(name = "mytest")
	    public Iterator<Object[]> parameterIntTestProvider() {
	    	List<TestBean> testBeans=new ArrayList<TestBean>();
	    	for(int i=0;i<100;i++){
	    		TestBean bean = new TestBean();
	    		bean.setName("testname");
	    		bean.setMsg("--"+i);
	    		bean.setSex("男");
	    		testBeans.add(bean);
	    	}
	    	
	    	List<Object[]> dataProvider = new ArrayList<Object[]>();
	    	testBeans.forEach(d->dataProvider.add(new Object[] {d}));
	        return dataProvider.iterator();
	    }
	    
	    
}
