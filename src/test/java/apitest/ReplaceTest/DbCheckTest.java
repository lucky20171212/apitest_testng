package apitest.ReplaceTest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.alibaba.fastjson.JSON;
import com.github.checkpoint.CheckPointUtils;
import com.github.checkpoint.JsonCheckResult;

import utils.JdbcUtils;
import utils.ParamUtils;

public class DbCheckTest {
	
	
	public static void main(String[] args) {
		
		//String dbCheck="select * from t_user_test,size>1,mysql";
		ParamUtils.addFromMap("id2", "test");
		String dbCheck="select * from t_user_test where uid='${id2}',size>1,mysql";
		dbCheck = ParamUtils.replace(dbCheck);
		//思路  数据框架 数据管理工具  数据库检查点  参数化
		String[] db_array =dbCheck.split(",");
		String sql=db_array[0];
		String db=db_array[2];
		String checkPoint=db_array[1];
		DataSource dataSource =JdbcUtils.getDataSource(db);
		QueryRunner runner = new QueryRunner(dataSource);
		try {
		   List<Map<String, Object>> listMap=runner.query(sql, new MapListHandler());
		   String json =JSON.toJSONString(listMap);
		   CheckPointUtils.openLog=true;
		   JsonCheckResult checkResult= CheckPointUtils.check(json, checkPoint);
		   System.out.println(checkResult.getMsg());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
