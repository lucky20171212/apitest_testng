package apitest.thread;

class MyRunable implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}

public class RunableTest {
	
	public static void main(String[] args) {
		
		
		new Thread(new MyRunable()).start();
		
		//接口不能new  如果new 需要匿名实现
	     new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		}).start();
	     
	     //函数写法
	     new Thread(()->{
	    	 System.out.println("1111");
	     }).start();
	     
	     //一步到位
	     new Thread() {
			@Override
			public void run() {
			}
	     }.start();
	     
	    
	}

}
