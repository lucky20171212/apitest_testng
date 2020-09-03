package utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class StringToMapUtils {
	
	
	public static Map<String, Object> covertStringToMap(String str,String regx) {
		if(StringUtils.isNotBlank(str)) {
			Map<String, Object> map=new LinkedHashMap<String, Object>();
			String[] str_arrsy=str.split(regx);
			for (String key : str_arrsy) {
				String[] key_array=key.split("=");
				if(key_array.length==2) {
					map.put(key_array[0], key_array[1]);
				}else if(key_array.length==1) {
					map.put(key_array[0], "");
				}
			}
			return map;
		}
		return null;
	}
	
	public static String getFromMap(Map<String, Object>map,String key) {
		Object mapValueObject=map.get(key);
    	if(mapValueObject==null) {
    		return "";
    	}else {
    	  return String.valueOf(mapValueObject);
    	}
	}

}
