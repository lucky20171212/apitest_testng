package utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.github.checkpoint.CheckPointUtils;
import com.github.checkpoint.JsonCheckResult;

public class DbCheckUtils {
	
	
	public static String check(String dbcheck) {
		if(StringUtils.isNotBlank(dbcheck)) {
			System.out.println(dbcheck);
			String[] dbcheck_array = dbcheck.split(",");
			String sql="";
			String dbcheckpoint="";
			if(dbcheck_array.length>=2) {
				sql=dbcheck_array[0];
				dbcheckpoint=dbcheck_array[1];
				String dbType="";
				if(!StringUtils.isEmpty(dbcheckpoint)) {
					if(dbcheck_array.length==3) {
						dbType=dbcheck_array[2];
					}
				QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource(dbType));
				try {
					List<Map<String,Object>> map = runner.query(sql, new MapListHandler());
					CheckPointUtils.openLog =true;
					JsonCheckResult result = CheckPointUtils.check(JSON.toJSONString(map), dbcheckpoint);
					return result.getMsg();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			}
		}
		
		return "没有设置数据库检查点";
	}
	
	
	public static void main(String[] args) {
		//ParamUtils.addMap("id", "21da27ab-1237-4e22-95ee-11379a7ba333");
		//String dbcheck ="select * from t_user_test2,size>1,mysql1";
		String dbcheck2="select * from  t_user_test,loginname[1]=James,mysql1";
		String checkResult = check(dbcheck2);
		System.out.println(checkResult);
	}
	

}
