package apitest.function;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import utils.ParamUtils;

public class FunctionTest {
	
	static String pattern = "\\#\\{(.+?)\\}"; // 非贪婪
	
	//__md5
	//__time
	//__sha
	//__shanext
	public static void main(String[] args) {
		 ParamUtils.addFromMap("loginname", "abc");
		 ParamUtils.addFromMap("loginpass", "abc");
		 String str="http://baidu.com?t=#{__time}&id=#{__UUID}&userId=#{__shanext(${loginname},${loginpass},121,1212,1212,shaUserId)}";
		 str=ParamUtils.replace(str);
		 System.out.println(str);
//		 Pattern r = Pattern.compile(pattern);
//		 Matcher m= r.matcher(str);
//		 while (m.find()) {
//			System.out.println(m.group());
//			System.out.println(m.group(1));
//			str=str.replace(m.group(), getFuntionValue(m.group(1)));
//		}
//		 System.out.println(str);
//		 String str2="http://baidu2.com?userId=${shaUserId}";
//		 str2= ParamUtils.replace(str2);
//		 System.out.println("----------"+str2);
//		//System.out.println(System.currentTimeMillis());
//		//String str="http://baidu.com?t=121212121212";
//		 System.out.println(UUID.randomUUID().toString());
	}
	
	
	private static String getFuntionValue(String funtionKey) {
		if(funtionKey.equalsIgnoreCase("__time")) {
			return ""+System.currentTimeMillis();
		}else if (funtionKey.equalsIgnoreCase("__UUID")) {
			return UUID.randomUUID().toString();
		}else if (funtionKey.startsWith("__md5")) {
			//参数提取出来
			//(abc,abc) \\((.+?)\\) 
			Pattern r = Pattern.compile("\\((.+?)\\)");
			Matcher m= r.matcher(funtionKey);
			 if (m.find()) {
//					System.out.println(m.group());
//					System.out.println(m.group(1));
					String arg=m.group(1);
					String[]args=arg.split(",");
					return DigestUtils.md5Hex(args[0]+args[1]).toUpperCase();
				}
			
		}else if (funtionKey.startsWith("__sha")) {
			//参数提取出来
			//(abc,abc) \\((.+?)\\) 
			Pattern r = Pattern.compile("\\((.+?)\\)");
			Matcher m= r.matcher(funtionKey);
			 if (m.find()) {
//					System.out.println(m.group());
//					System.out.println(m.group(1));
					String arg=m.group(1);
					String[]args=arg.split(",");
					String shahex= DigestUtils.sha1Hex(args[0]+args[1]).toUpperCase();
					ParamUtils.addFromMap(args[2], shahex);
					return shahex;
				}
			
		}
		return "";
	}

}
