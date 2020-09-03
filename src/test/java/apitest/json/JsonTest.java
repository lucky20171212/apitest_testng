package apitest.json;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonTest {
	
	public static void main(String[] args) {
		String str="{\"msg\":\"登录成功\",\"uid\":\"9CC972DFA2D4481F89841A46FD1B3E7B\",\"code\":\"1\"}";
		JSONObject jsonObject = (JSONObject) JSON.parse(str);
		System.out.println(jsonObject.get("msg"));
		System.out.println(jsonObject.get("uid"));
		
		//反射
		Result result =JSON.parseObject(str, Result.class);
		System.out.println(result);
		
		
		String str2="[{\"loginname\":\"abc0\",\"loginpass\":\"abc0\"}"
				+ ",{\"loginname\":\"abc1\",\"loginpass\":\"abc1\"},"
				+ "{\"loginname\":\"abc2\",\"loginpass\":\"abc2\"}]\r\n" + 
				"";
		
		JSONArray jsonObject2= (JSONArray) JSON.parse(str2);
		for (int i = 0; i < jsonObject2.size(); i++) {
			JSONObject jsonObject3 =(JSONObject) jsonObject2.get(i);
			System.out.println(jsonObject3);
		}
		
		List<User> list=JSON.parseArray(str2, User.class);
		
		//字符串
		System.out.println(list);  //toString
		
		System.out.println(JSON.toJSONString(list)); //json
	}
	

}
