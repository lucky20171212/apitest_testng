package apitest.ReplaceTest;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.HttpClientUtils;
import utils.ParamUtils;
import utils.StringToMapUtils;

public class FromHtml {
	
	public static void main(String[] args) {
		
		String html = HttpClientUtils.doGet("http://www.baidu.com");
		// String html = "This order was placed for QT3000! OK?";
		 //提取数字  3000
		//String regx ="id=QT(.+?)!";
		String regx ="title=<title>(.+?)</title>";
		getFromByPattern(html,regx);
		
		Map<String, Object> map=StringToMapUtils.covertStringToMap(regx, ";");
		
		System.out.println(map);
	
		//map(id,)
		String url="http://www.baidu.com?test=${title}";
		url=ParamUtils.replace(url);
		System.out.println(url);
		// String regx ="title=<title>(.+?)</title>";
		// getFromHtml(html,regx);
	}
	
	private static void getFromByPattern(String txt,String regx) {
		Map<String, Object> map=StringToMapUtils.covertStringToMap(regx, ";");
		map.forEach((k,v)->{
			 Pattern r = Pattern.compile(v.toString());
			 Matcher m = r.matcher(txt);
			 while(m.find()) {
				System.out.println(m.group()); 
				System.out.println(m.group(1)); 
				ParamUtils.addFromMap(k, m.group(1));
			 }
		});
	}
	
	
//	
//	private static void getFromHtml(String html,String regx) {
//		Map<String, Object> map=StringToMapUtils.covertStringToMap(regx, ";");
//		Set<String> keySet=map.keySet();
//		for (String key : keySet) {
//		   String value =(String) map.get(key);
//			   Pattern r = Pattern.compile(value);
//			    Matcher m = r.matcher(html);
//			    while(m.find()) {
//			    	System.out.println(m.group());
//			    	System.out.println(m.group(1));
//			}
//		}
//	}

}
