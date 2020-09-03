package apitest.json;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class User {
	
	private String loginname;
	
	@JSONField(name="loginpass")
	private String pass;
}
