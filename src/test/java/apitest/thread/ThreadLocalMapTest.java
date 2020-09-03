package apitest.thread;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalMapTest {
	
	static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>(){

		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};
	
//	static ThreadLocal<String> threadLocalStringLocal =new  ThreadLocal<String>(){
//
//		@Override
//		protected String initialValue() {
//			// TODO Auto-generated method stub
//			return super.initialValue();
//		}
//		
//	};
	public static void main(String[] args) {
		//主方法是主线程
		threadLocal.get().put("1","11");
		dosomethings();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				//线程内部
				threadLocal.get().put("1","22");
				dosomethings();
			}
		}).start();
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				threadLocal.get().put("1","33");
				threadLocal.get().put("2","44");
				dosomethings();
			}
		}).start();
	}
	
	private static void dosomethings() {
		System.out.println(Thread.currentThread().getName() + "  " +threadLocal.get());
	}

}
