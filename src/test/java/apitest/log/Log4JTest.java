package apitest.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4JTest {
	
	private static final Logger logger = LoggerFactory.getLogger(Log4JTest.class);
	
	public static void main(String[] args) {
//		logger.debug("debug");
//		logger.info("info");
//		logger.warn("warn");
//		logger.error("error");
		while(true) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error");
		}
	}

}
