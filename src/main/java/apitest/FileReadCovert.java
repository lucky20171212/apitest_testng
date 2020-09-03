package apitest;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.crab2died.converter.ReadConvertible;

//解耦
public class FileReadCovert implements ReadConvertible{

	@Override
	public Object execRead(String object) {
		//结尾是否 包含 txt csv json
		if(StringUtils.endsWithAny(object, "txt","csv","json")) {
			String path=System.getProperty("user.dir")+File.separator+"data"+File.separator+object;
			try {
				return FileUtils.readFileToString(new File(path), "utf-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return object;
		
	}

}
