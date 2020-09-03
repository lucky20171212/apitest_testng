package apitest.thread;

import java.util.concurrent.CountDownLatch;

class MyThread extends Thread{
	
	private CountDownLatch countDownLath;
	

	public MyThread(CountDownLatch countDownLath) {
		super();
		this.countDownLath = countDownLath;
	}



	//cpu 消耗cpu  指标下降
	@Override
	public void run() {
		//线程内部 
		for (int i = 0; i < 10; i++) {
			System.out.println(Thread.currentThread().getName()+" "+i);
		}
		//线程休眠10毫秒
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		countDownLath.countDown();
//		
//		while(true) {
//			
//		}
	}
	
}

public class ThreadTest {
	
	public static void main(String[] args) {
		System.out.println("开始");
		
		//启动了10个线程 并行 同时开启了10个跑道，每个跑道齐头并进
		//cpu 调度 2核心 4核心
		CountDownLatch countDownLath  =new CountDownLatch(10);
	    long start=System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			new MyThread(countDownLath).start();
		}
		
		try {
			//count=0
			countDownLath.await();
			System.out.println("线程最大花费时间"+(System.currentTimeMillis()-start));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			//1000毫秒 1秒 时间不好控制
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		System.out.println("结束");
		
	}
	
	private static void doSomethings() {
		System.out.println("测试");
	}

}
