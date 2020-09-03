package apitest.ReplaceTest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.alibaba.fastjson.JSON;
import com.github.checkpoint.CheckPointUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import utils.JdbcUtils;

public class DbTest {
	
	//连接池  默认读 src/main/resources/c3p0-config.xml
	//private static ComboPooledDataSource ds = new ComboPooledDataSource();
	 
	private static ComboPooledDataSource ds = new ComboPooledDataSource("mysql");
	
	private static Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//jdbc
	private static DbUser getByIdAndC3po(String id) {
		Connection conn=getConnection();
		try {
//			//加载数据库驱动（找连接工具类）
//			Class.forName("com.mysql.jdbc.Driver");
//			//连接数据库
//			conn = DriverManager.getConnection
//			("jdbc:mysql://118.24.13.38:3307/testfan_jiguang?characterEncoding=utf8&useSSL=false", "root", "123456");
			//创建执行sql对象
			String sql="select * from t_user_test where uid=?";
			//预编译 防止sql安全注入
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet set=statement.executeQuery();
			DbUser dbUser=new DbUser();
			if (set.next()) {
				dbUser.setUid(set.getString("uid"));
				dbUser.setLoginName(set.getString("loginname"));
				dbUser.setLoginPass(set.getString("loginpass"));
		    }
			return dbUser;
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
		return null;
	}
	
	private static DbUser getById(String id) {
		Connection conn=null;
		try {
			//加载数据库驱动（找连接工具类）
			Class.forName("com.mysql.jdbc.Driver");
			//连接数据库
			conn = DriverManager.getConnection
			("jdbc:mysql://118.24.13.38:3307/testfan_jiguang?characterEncoding=utf8&useSSL=false", "root", "123456");
			//创建执行sql对象
			String sql="select * from t_user_test where uid=?";
			//预编译 防止sql安全注入
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet set=statement.executeQuery();
			DbUser dbUser=new DbUser();
			if (set.next()) {
				dbUser.setUid(set.getString("uid"));
				dbUser.setLoginName(set.getString("loginname"));
				dbUser.setLoginPass(set.getString("loginpass"));
		    }
			return dbUser;
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
		return null;
	}
	
	
	private static List<DbUser> getUserList() {
		Connection conn=null;
		List<DbUser> list=null;
		try {
			//加载数据库驱动（找连接工具类）
			Class.forName("com.mysql.jdbc.Driver");
			//连接数据库
			conn = DriverManager.getConnection
			("jdbc:mysql://118.24.13.38:3307/testfan_jiguang?characterEncoding=utf8&useSSL=false", "root", "123456");
			//创建执行sql对象
			String sql="select * from t_user_test";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet set=statement.executeQuery();
			 list =new ArrayList<DbUser>();
			while (set.next()) {
				DbUser dbUser=new DbUser();
				dbUser.setUid(set.getString("uid"));
				dbUser.setLoginName(set.getString("loginname"));
				dbUser.setLoginPass(set.getString("loginpass"));
				list.add(dbUser);
		    }
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
		return list;
	}
	
	//dbutils
	private static List<DbUser> getUserListDbUtils() {
	    QueryRunner runner = new QueryRunner(ds);
		String sql="select * from t_user_test";
		try {
			List<DbUser> dbUserList =(List<DbUser>) runner.query(sql, new BeanListHandler(DbUser.class));
			return dbUserList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	private static void getUserListDbUtilsMap() {
	    QueryRunner runner = new QueryRunner(ds);
		String sql="select * from t_user_test";
		try {
			List<Map<String, Object>> dbUserList =runner.query(sql, new MapListHandler());
			System.out.println(JSON.toJSONString(dbUserList));
			CheckPointUtils.check(JSON.toJSONString(dbUserList), "size>1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static DbUser getByIdDbUtils(String uid) {
	    QueryRunner runner = new QueryRunner(ds);
		String sql="select * from t_user_test where uid=?";
		try {
			DbUser dbUser = (DbUser) runner.query(sql, new BeanHandler(DbUser.class),uid);
			return dbUser;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void getByIdDbUtilsMap(String uid) {
	    QueryRunner runner = new QueryRunner(ds);
		String sql="select * from t_user_test where uid=?";
		try {
			Map map = runner.query(sql, new MapHandler(),uid);
			System.out.println(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void listloingName() {
	    QueryRunner runner = new QueryRunner(ds);
		String sql="select loginname from t_user_test";
		try {
			List list = (List) runner.query(sql, new ColumnListHandler("loginname"));
			System.out.println(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void delete() throws SQLException {
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "delete from t_user_test where loginname=? and loginpass=?";
		//runner.update(sql, "zhangsan","张三");
		runner.update(sql, new Object[] {"00","22"});
	}
	
	
	public static void main(String[] args) {
//	   DbUser dbUser=getById("4D4FAB150B7451407198111");
//	   System.out.println(dbUser);
	   
//	  List<DbUser> list= getUserList();
//	  list.forEach(d->System.out.println(d));
	  
//	  DbUser dbUser = getByIdAndC3po("4D4FAB150B7451407198111");
//	  System.out.println(dbUser);
//	  
//	  DbUser dbUser2=  getByIdDbUtils("4D4FAB150B7451407198111");
//	  System.out.println(dbUser2);
	  
//	  List<DbUser> list= getUserListDbUtils();
//      list.forEach(d->System.out.println(d));
//      try {
//		delete();
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}
      
     // getByIdDbUtilsMap("4D4FAB150B7451407198111");
     // getUserListDbUtilsMap();
      
      listloingName();
	}

}
