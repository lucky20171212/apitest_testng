package apitest;

import com.github.crab2died.annotation.ExcelField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//有一部分数据来源来TestCase
@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)  //继承后是否使用父类属性
public class TestCaseResult extends TestCase{
	
	@ExcelField(title = "数据库检查点结果",order = 3)
	private String dbCheckResult;
	
	@ExcelField(title = "接口返回检查结果",order = 4)
	private String apiCheckResult;

}
