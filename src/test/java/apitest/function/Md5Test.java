package apitest.function;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Test {
	
	public static void main(String[] args) {
       try {
		System.out.println(DigestUtils.md5Hex("123".getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	}

}
