# 第1部分 环境搭建

## 1.1 基础工程搭建

  excel4j+httpclient

```xml
     <dependency>
			<groupId>com.github.crab2died</groupId>
			<artifactId>Excel4J</artifactId>
			<version>3.0.0</version>
		</dependency>

		<dependency>
			<groupId>org.apache.httpcomponents</groupId>
			<artifactId>httpclient</artifactId>
			<version>4.5.12</version>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.12</version>
			<scope>provided</scope>
		</dependency>
```

## 1.2 基础类编写

TestCase (业务实体)

### 1.2.1 ApiTest 

集成HttpclientUtils

```java
package apitest;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;

import utils.HttpClientUtils;

public class ApiTest {
	
	public static void main(String[] args) {
		String path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"apitest2.xlsx";
	     try {
			List<TestCase> list = ExcelUtils.getInstance().readExcel2Objects(path, TestCase.class);
			//过滤
			List<TestCase> filterList= list.stream().filter(testCase->"是".equals(testCase.getIsOpen())).collect(Collectors.toList());
			//排序
			Collections.sort(filterList, (o1,o2)->o1.getOrder()-o2.getOrder());
			String result="";
			for (TestCase testCase : filterList) {
				if("get".equals(testCase.getType())) {
					result = HttpClientUtils.doGet(testCase.getUrl());
				}else if ("post".equals(testCase.getType())) {
					result =HttpClientUtils.doPost(testCase.getUrl(), testCase.getParams());
				}
				System.out.println(result);
			}
			//System.out.println(list);
		} catch (Excel4JException | IOException e) {
			e.printStackTrace();
		}
	}
}
```

