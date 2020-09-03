package apitest.ReplaceTest;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.checkpoint.CheckPointUtils;

import utils.ParamUtils;

public class XmlTest {

	public static void main(String[] args) {
		 XmlMapper xmlMapper = new XmlMapper();
		 //json
		 try {
//			 User user=new User(1,"lucas","nan","2019-10-01","上海");
//		     String xml_user=xmlMapper.writeValueAsString(user);
//		     System.out.println(xml_user);
			 
			 String xml_user="<User><id>1</id><username>lucas</username><sex>nan</sex><birthday>2019-10-01</birthday><address>上海</address></User>";
		     
		     User userObject=xmlMapper.readValue(xml_user,User.class);
		     System.out.println(userObject);
		     
		     //xml 格式数据
			 String xml="<returnsms>" + 
			 		" 	<returnstatus>Success</returnstatus>" + 
			 		" 	<message>ok</message>" + 
			 		" 	<remainpoint>11032</remainpoint>" + 
			 		" 	<taskID>4354699</taskID>" + 
			 		" 	<test>123</test>" + 
			 		" 	<successCounts>1</successCounts>"+
			 		"</returnsms>";
			 //转json
			 Map map =xmlMapper.readValue(xml, HashMap.class);
			 System.out.println(map);
			 //taskId="123"
			 //xml如何转json?
			String jsonString=JSON.toJSONString(map);
			System.out.println(jsonString);
			
			ParamUtils.addFromJson(jsonString, "id=taskID");
			ParamUtils.println();
			CheckPointUtils.check(jsonString, "taskID=4354699");
			 
			 //CheckPointUtils
			  
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
