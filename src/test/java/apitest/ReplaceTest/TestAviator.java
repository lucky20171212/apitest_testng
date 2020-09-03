package apitest.ReplaceTest;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;

public class TestAviator {

	public static void main(String[] args) {

		System.out.println("1+2+3+6");
		Long result = (Long) AviatorEvaluator.execute("1+2+3+6");
		System.out.println(result);

		//jsonpath map数据源问题
		Map<String, Object> env = new HashMap<String, Object>();
		env.put("data1", 2);
		env.put("data2", 2);
		Boolean result1 = (Boolean) AviatorEvaluator.execute("data1>=3||data2>1", env);
		System.out.println(result1);
	}

}
