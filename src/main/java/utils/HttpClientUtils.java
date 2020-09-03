package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
	
	private static CloseableHttpClient closeableHttpClient;
	
	public static boolean openProxy=false;
	
	private static CloseableHttpClient init() {
		if(closeableHttpClient!=null) {
			return closeableHttpClient;
		}
		//支持https 代理
		closeableHttpClient =SslUtil.SslHttpClientBuild(openProxy);
		return closeableHttpClient;
	}
	
	//重载
	public static String doGet(String url) {
		return doGet(url,null);
	}
	
	public static String doGetMap(String url,Map<String, Object> header) {
		CloseableHttpClient closeableHttpClient =init();
		HttpGet get=new HttpGet(url);
		if(header!=null&&!header.isEmpty()) {
			 header.forEach((k,v)->get.addHeader(k, v.toString()));
		 }
		CloseableHttpResponse closeableHttpResponse=null;
		try {
			 closeableHttpResponse= closeableHttpClient.execute(get);
			 StatusLine statusLine= closeableHttpResponse.getStatusLine();
			if(statusLine.getStatusCode()==200) {
				HttpEntity httpEntity =closeableHttpResponse.getEntity();
				String result = EntityUtils.toString(httpEntity,"utf-8");
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(closeableHttpResponse!=null) {
				try {
					closeableHttpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(header!=null) {
					header.clear();
				}
			}
		}
		return "error";
	}
	
	public static String doGet(String url,String header) {
		return doGetMap(url, StringToMapUtils.covertStringToMap(header, ";"));
	}
	
	public static String doPost(String url, String params) {
		return doPost(url,params,"");
	}
	
	
	//http://118.24.13.38:8080/goods/UserServlet
	
	// method=loginMobile&loginname=test1&loginpass=test1
	public static String doPost(String url, String params,String header) {
		return doPostMap(url, StringToMapUtils.covertStringToMap(params,"&"), StringToMapUtils.covertStringToMap(header,";"));
	}
	
	//重点准备 手写httpclient
	public static String doPostMap(String url, Map<String, Object> params, Map<String, Object> header) {
		CloseableHttpClient closeableHttpClient =init();
		HttpPost post=new HttpPost(url);
		CloseableHttpResponse closeableHttpResponse=null;
		try {
			if(params!=null&&!params.isEmpty()) {
				List<NameValuePair> list=new ArrayList<NameValuePair>();
				params.forEach((k,v)->{
					list.add(new BasicNameValuePair(k,v.toString()));
				});
				HttpEntity postEntity =new UrlEncodedFormEntity(list,"utf-8");
				post.setEntity(postEntity);
			}
			
			if(header!=null&&!header.isEmpty()) {
				header.forEach((k,v)->post.addHeader(k, v.toString()));
			}
			//body
			 closeableHttpResponse= closeableHttpClient.execute(post);
			 StatusLine statusLine= closeableHttpResponse.getStatusLine();
			 System.out.println(statusLine.getStatusCode());
			if(statusLine.getStatusCode()==200) {
				HttpEntity httpEntity =closeableHttpResponse.getEntity();
				String result = EntityUtils.toString(httpEntity,"utf-8");
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(closeableHttpResponse!=null) {
				try {
					closeableHttpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(params!=null) {
				params.clear();
			}
			
			if(header!=null) {
				header.clear();
			}
		}
		return "error";
		
	}
	
	public static String doPostJson(String url, String params, String header) {
		return doPostJson(url, params, StringToMapUtils.covertStringToMap(header, ";"));
	}
	
	public static String doPostJson(String url, String params, Map<String,Object> header) {
		CloseableHttpClient closeableHttpClient =init();
		HttpPost post=new HttpPost(url);
		CloseableHttpResponse closeableHttpResponse=null;
		try {
			//json
			HttpEntity postEntity =new StringEntity(params, "utf-8");
			post.setEntity(postEntity);
			post.setHeader("Content-type", "application/json");
			if(header!=null&&!header.isEmpty()) {
				header.forEach((k,v)->post.addHeader(k, v.toString()));
			}
			  //body
			 closeableHttpResponse= closeableHttpClient.execute(post);
			 StatusLine statusLine= closeableHttpResponse.getStatusLine();
			 System.out.println(statusLine.getStatusCode());
			if(statusLine.getStatusCode()==200) {
				HttpEntity httpEntity =closeableHttpResponse.getEntity();
				String result = EntityUtils.toString(httpEntity,"utf-8");
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(closeableHttpResponse!=null) {
				try {
					closeableHttpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "error";
		
	}
	
	public static void main(String[] args) {
		HttpClientUtils.openProxy =true;
		Map<String, Object> header =new HashMap<String, Object>();
		header.put("key", "123");
		header.put("key2", "test");
		header.put("key3", "test");
		doGetMap("http://baidu.com", header);
		
		Map<String, Object> params =new HashMap<String, Object>();
		params.put("method", "loginMobile");
		params.put("loginname", "abc");
		params.put("loginpass","abc");
		doPostMap("http://118.24.13.38:8080/goods/UserServlet",params,header);
	}
	

}
