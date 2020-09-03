package apitest.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {

	public static void main(String[] args) {
		
//		for (int i = 0; i < 1000; i++) {
//			final int index=i;
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					System.out.println(Thread.currentThread().getName()+" i---"+ index);
//				}
//			}).start();
//		}
		
		//开的窗口数目+排队 
		//无限开, 短任务
	     ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		
		//按照固定的数目
		 ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		 
		 //默认一个
		 ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		
		 //start 没有？
		for (int i = 0; i < 10; i++) {
			final int index=i;
			singleThreadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName()+" i---"+ index);
				}
			});
		}
		
		System.out.println("111111111");
//		
		
//	
//		 
//		 //默认一个
//		 ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
//
//		for (int i = 0; i < 10; i++) {
//			final int index = i;
//			//放了十个线程
//			fixedThreadPool.execute(new Runnable() {
//				public void run() {
//					System.out.println(index+Thread.currentThread().getName());
//				}
//			});
//		}
//		fixedThreadPool.shutdown();
//		
//		//延迟持续，重复执行
//	    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
//		 scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				System.out.println("111111");
//			}
//		},0,1,TimeUnit.SECONDS);
	}

}
