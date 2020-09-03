package utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

public class ParamUtils {
	
	//全局最大问题会有数据混乱
	//private static Map<String, Object> correlation =new LinkedHashMap<String, Object>();
	
	
	private static ThreadLocal<Map<String, Object>> correlation =new ThreadLocal<Map<String,Object>>(){

		@Override
		protected Map<String, Object> initialValue() {
			return new LinkedHashMap<String, Object>();
		}
		
	};
	
	public static void clear() {
		correlation.get().clear();
	}
	
	public static void println() {
		System.out.println(correlation);
	}
	
	public static void addFromMap(String key, Object value) {
		correlation.get().put(key, value);
	}
	
	public static void addFromMap(Map<String, Object> map) {
		if(map!=null&&!map.isEmpty()) {
			correlation.get().putAll(map);
			map.clear();
		}
	}
	
	public static void addCorrelationParams(String result, String regxJson,String regxPattern) {
		 addFromJson(result, regxJson);
	     addFromText(result, regxPattern);
	}
	
	
	//title=<title>(.+?)</title>
	public static void addFromText(String txt, String regx) {
		if(StringUtils.isNotBlank(regx)&&StringUtils.isNotBlank(txt)) {
			Map<String, Object> regxmapMap= StringToMapUtils.covertStringToMap(regx, ";");
			regxmapMap.forEach((k,v)->{
				 Pattern r = Pattern.compile(v.toString());
				 Matcher m = r.matcher(txt);
				 //多个
				 while(m.find()) {
					 correlation.get().put(k, m.group(1));
				 }
			});
			regxmapMap.clear();
		}
	}
	
	//{"msg":"登录成功","uid":"9CC972DFA2D4481F89841A46FD1B3E7B","code":"1"}
	//id2=uid;code=code
	//{code=1, id2=9CC972DFA2D4481fasfasfaf46FD1B3E7B}
	public static void addFromJson(String json, String regx) {
		if (JSON.isValid(json)) {
			System.out.println("addFromJson"+json +" regx "+regx);
		Map<String, Object> regxMap= StringToMapUtils.covertStringToMap(regx, ";");
		if(regxMap!=null&&!regxMap.isEmpty()) {
			regxMap.forEach((k,v)->{
				String jsonpath=String.valueOf(v);
				//绝对路径查找
				Object object= JSONPath.read(json, jsonpath);
				System.out.println(jsonpath+ "jsonpath "+object);
				if(object==null) {
					//相对路径再去查找下
					object = JSONPath.read(json, ".."+jsonpath);
				}
				System.out.println(object);
				if(object instanceof List) {
					List<Object> list =(List)object;
					for (int i = 0; i < list.size(); i++) {
						correlation.get().put(k+"_g"+(i+1), list.get(i));
					}
				}else {
					correlation.get().put(k, JSONPath.read(json,String.valueOf(v)));
				}
			});
			System.out.println("当前数据"+correlation);
		    regxMap.clear();
		 }
		}
	}
	
	static String pattern = "\\$\\{(.*?)\\}";

	public static String replace(String str) {
		if(StringUtils.isNotBlank(str)) {
			 Pattern r2 = Pattern.compile(pattern);
			    Matcher m2= r2.matcher(str);
			    while(m2.find()) {
			    	String map_key=m2.group(1);
			    	str =str.replace(m2.group(), StringToMapUtils.getFromMap(correlation.get(), map_key));
			    }
		}
		// return str;
		 return FunctionUtils.function(str);
		  
	}
	
	public static void main(String[] args) {
		String json="{\"msg\":\"登录成功\",\"uid\":\"9CC972DFA2D4481F89841A46FD1B3E7B\",\"code\":\"1\"}";
		ParamUtils.addFromJson(json, "code=code");
//		
//		String json2="{\"msg\":\"登录成功\",\"uid\":\"9CC972DFA2D4481fasfasfaf46FD1B3E7B\",\"code\":\"1\"}";
//		ParamUtils.addFromJson(json2, "id2=uid");
//		
//		
//		Map map=new HashMap<String, Object>();
//		map.put("loginname", "abc");
//		map.put("loginpass", "abc");
//		ParamUtils.addFromMap(map);
		
		//
		String json3="{\"code\":\"1\",\"data\":[{\"name\":\"testfan0\",\"pwd\":\"pwd0\"},{\"name\":\"testfan1\",\"pwd\":\"pwd1\"},{\"name\":\"testfan2\",\"pwd\":\"pwd2\"}]}";
		ParamUtils.addFromJson(json3, "names=name");
		//names_g1 g2
		System.out.println(correlation);
	}

}
