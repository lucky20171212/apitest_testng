package apitest.function;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64; //
import org.apache.commons.codec.binary.Hex;//16
import org.apache.commons.codec.digest.DigestUtils;

public class CodecTest {

	public static void main(String[] args) {
		String str = "test";
//		String reuslt1 = encodeTest(str);
//		decodeTest(reuslt1);

//		String reuslt2 = encodeHexTest(str);
//		decodeHexTest(reuslt2);
//
//	   MD5Test(str);
//	
//		ShaTest(str);
	
		System.out.println(DigestUtils.md5Hex("123"+System.currentTimeMillis()));
		System.out.println(DigestUtils.md5Hex("123"+System.currentTimeMillis()));

	}

	private static String encodeTest(String str) {
		Base64 base64 = new Base64();
		try {
			str = base64.encodeToString(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("Base64 编码后：" + str);
		return str;
	}

	private static void decodeTest(String str) {
		Base64 base64 = new Base64();
		str = new String(Base64.decodeBase64(str));
		System.out.println("Base64 解码后：" + str);
	}

	private static String encodeHexTest(String str) {
		try {
			str = Hex.encodeHexString(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("Hex 编码后：" + str);
		return str;
	}

	private static String decodeHexTest(String str) {
		Hex hex = new Hex();
		try {
			str = new String((byte[]) hex.decode(str));
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		System.out.println("Hex 编码后：" + str);
		return str;
	}

	//摘要 唯一指纹
	private static String MD5Test(String str) {
		try {
			System.out.println("MD5 编码后：" + new String(DigestUtils.md5Hex(str.getBytes("UTF-8"))));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	//sha 长度更大，更安全
	private static String ShaTest(String str) {
		try {
			System.out.println("SHA 编码后：" + new String(DigestUtils.shaHex(str.getBytes("UTF-8"))));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

}
