package utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class FunctionUtils {

	static String pattern = "\\#\\{(.+?)\\}"; // 非贪婪
	static Pattern r = Pattern.compile(pattern);

	public static String function(String str) {
		if (StringUtils.isNoneBlank(str)) {
			Matcher matcher = r.matcher(str);
			while (matcher.find()) {
				String toReplace = matcher.group();
				String funtionKey = matcher.group(1);
				str = str.replace(toReplace, getFuntionValue(funtionKey));
			}
		}
		return str;
	}

	private static String getFuntionValue(String funtionKey) {
		if ("__UUID".equalsIgnoreCase(funtionKey)) {
			return UUID.randomUUID().toString();
		} else if ("__time".equalsIgnoreCase(funtionKey)) {
			return "" + System.currentTimeMillis();
		} else if (funtionKey.startsWith("__md5")) {
			// __md5(abc,abc) ((.+?)) split
			String[] funtion_arg = getFuntionArgs(funtionKey);
			String md5 = "";
			if (funtion_arg != null && funtion_arg.length > 1) {
				md5 = DigestUtils.md5Hex(funtion_arg[0] + funtion_arg[1]);
			}
			if (funtion_arg.length > 2) {
				ParamUtils.addFromMap(funtion_arg[2], md5);
			}
			return md5;
		}else if (funtionKey.startsWith("__sha")) {
			String[] funtion_arg = getFuntionArgs(funtionKey);
			String sha1Hex = "";
			if (funtion_arg != null && funtion_arg.length > 1) {
				String allKey="";
				for (int i = 0; i < funtion_arg.length; i++) {
					allKey+=funtion_arg[i];
				}
				sha1Hex= DigestUtils.sha1Hex(allKey).toUpperCase();
			}
			//ParamUtils.addFromMap(funtion_arg[funtion_arg.length-1], sha1Hex);
//			if (funtion_arg.length > 2) {
//				ParamUtils.addFromMap(funtion_arg[length-1], sha1Hex);
//			}
			return sha1Hex;
		}
		return "";
	}

	private static String[] getFuntionArgs(String funtionKey) {
		String pattern = "\\((.+?)\\)"; // 非贪婪
		Pattern r = Pattern.compile(pattern);
		Matcher matcher = r.matcher(funtionKey);
		String[] funtion_arg = null;
		if (matcher.find()) {
			System.out.println(matcher.group(1));
			funtion_arg = matcher.group(1).split(",");
		}
		return funtion_arg;
	}

}
