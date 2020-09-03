package apitest.oom;


import java.util.ArrayList;

public class JavaHeapTest {
	//oom 线程给太多 队列满，内存太小 cpu （gc）
	 public static void main(String[] args)
	    {
	        ArrayList list=new ArrayList();
	        while(true)
	        {
	        	System.out.println("add ");
	            list.add(new JavaHeapTest());
	        }
	    }
}
