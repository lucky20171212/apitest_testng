package utils;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JdbcUtils {
	private static ComboPooledDataSource ds1 = new ComboPooledDataSource();
	private static ComboPooledDataSource ds2 = new ComboPooledDataSource("mysql");
	private static ComboPooledDataSource ds3 = new ComboPooledDataSource("oracle");
	
	
	public static DataSource getDataSource(String type) {
		if("mysql".equals(type)) {
			return ds2;
		}else if ("oracle".equals(type)) {
			return ds3;
		}
		return ds1;
	}
	
	public static DataSource getDataSource() {
		return getDataSource("");
	}

}
