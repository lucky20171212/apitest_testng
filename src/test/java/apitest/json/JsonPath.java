package apitest.json;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

public class JsonPath {

	public static void main(String[] args) {
	    String jsonStr = "{\n" +
                "    \"store\": {\n" +
                "        \"bicycle\": {\n" +
                "            \"color\": \"red\",\n" +
                "            \"price\": 19.95\n" +
                "        },\n" +
                "        \"book\": [\n" +
                "            {\n" +
                "                \"author\": \"刘慈欣\",\n" +
                "                \"price\": 8.95,\n" +
                "                \"category\": \"科幻\",\n" +
                "                \"title\": \"三体\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"itguang\",\n" +
                "                \"price\": 12.99,\n" +
                "                \"category\": \"编程语言\",\n" +
                "                \"title\": \"go语言实战\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
	   // System.out.println(jsonStr);
	    
//	   JSONObject jsonObject= (JSONObject) JSON.parse(jsonStr);
//	   System.out.println(jsonObject.get("store"));
//	   
//	   JSONObject jsonObject2= (JSONObject)jsonObject.get("store");
//	   
//	   jsonObject2.getJSONArray("book");
	   
	    //第一本书title
        String title = (String) JSONPath.read(jsonStr, "store.book[0].title");
        System.out.println(title);
        
        BigDecimal prDouble = (BigDecimal) JSONPath.read(jsonStr, "store.book[0].price");
        System.out.println(prDouble);
        
        List<String> titles =  (List<String>) JSONPath.read(jsonStr, "store.book.title");
        System.out.println(titles);
        
        System.out.println("绝对路径");
        //绝对路径 相对路径
        List<BigDecimal> prices =  (List<BigDecimal>) JSONPath.read(jsonStr, "$.store.book.price");
       for (BigDecimal bigDecimal : prices) {
		System.out.println(bigDecimal);
	   }
       System.out.println("相对路径");
        //多层结构 相对路径
        List<BigDecimal> prices2 =  (List<BigDecimal>) JSONPath.read(jsonStr, "..book.price");
        for (BigDecimal bigDecimal : prices2) {
			System.out.println(bigDecimal);
		}
 	}
}
