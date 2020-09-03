package apitest.ReplaceTest;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

public class IdCartTest {
	static String path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"test.txt";
	
	private static void countByJson() {
		try {
			int count=0;
			List<String> list=FileUtils.readLines(new File(path),"utf-8");
			for (String d : list) {
				JSONObject object =JSON.parseObject(d);
				if(StringUtils.isBlank(object.get("idcard").toString())) {
					count++;	
				}
			}
			System.out.println("countByJson 空值数目 "+count);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void countByJsonPath() {
		try {
			int count=0;
			List<String> list=FileUtils.readLines(new File(path),"utf-8");
			for (String d : list) {
				Object object= JSONPath.read(d, "idcard");
				if(StringUtils.isBlank(object.toString())) {
					count++;	
				}
			}
			System.out.println("countByJsonpath 空值数目 "+count);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void countByPattern() {
		try {
			 int count=0;
			 String file=FileUtils.readFileToString(new File(path),"utf-8");
			 Pattern r = Pattern.compile("\"idcard\":\"(.*?)\",");
			 Matcher m = r.matcher(file);
			 while(m.find()) {
				if(StringUtils.isBlank(m.group(1))){
					 count++;
				}
			 }
			System.out.println("countBy 正则 空值数目 "+count);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void countByDb() {
		Connection conn=null;
		try {
			//加载数据库驱动（找连接工具类）
			Class.forName("com.mysql.jdbc.Driver");
			//连接数据库
			conn = DriverManager.getConnection
			("jdbc:mysql://118.24.13.38:3307/testfan_jiguang?characterEncoding=utf8&useSSL=false", "root", "123456");
			//创建执行sql对象
			Statement statement = conn.createStatement();
			String sql="select count(1) as mycount from fan_test where idcard=''";
			ResultSet  set=statement.executeQuery(sql);
			int count=0;
			if (set.next()) {
				count = set.getInt("mycount");
		    }
			System.out.println("数据库统计 结果"+count);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public static void main(String[] args) {
//		countByJson();
//		countByJsonPath();
//		countByPattern();
		countByDb();
	}

}
