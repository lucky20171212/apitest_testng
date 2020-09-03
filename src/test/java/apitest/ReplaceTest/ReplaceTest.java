package apitest.ReplaceTest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.StringToMapUtils;

public class ReplaceTest {
	
	public static void main(String[] args) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("loginname", "abc");
		map.put("loginpass", "abc");
	   // String str="http://118.24.13.38:8080/goods/UserServlet?method=loginMobile&loginname=${loginname}&loginpass=${loginpass}&test=${test}";
	    //String str="http://118.24.13.38:8080/goods/UserServlet?method=loginMobile&loginname=${loginname}";
	    
	    String str="http://118.24.13.38:8080/goods/UserServlet?method=loginMobile";
	    //提取出来 正则表达式
	    
//	    String line = "This order was placed for QT3000! OK?";
//	    String pattern = "(\\D*)(\\d+)(.*)";
//	    Pattern r = Pattern.compile(pattern);
//	    Matcher m = r.matcher(line);
//	    if(m.find()) {
//	    	System.out.println(m.group());
//	    	System.out.println(m.group(1));
//	    	System.out.println(m.group(2));
//	    	System.out.println(m.group(3));
//	    
	    String pattern2 = "\\$\\{(.*?)\\}";
	    Pattern r2 = Pattern.compile(pattern2);
	    Matcher m2= r2.matcher(str);
	    while(m2.find()) {
	    	System.out.println(m2.group());
	    	String key=m2.group(1);
	    	str =str.replace(m2.group(), StringToMapUtils.getFromMap(map, key));
	    }
	    System.out.println(str);
	}

}
