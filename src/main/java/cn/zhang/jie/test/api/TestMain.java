package cn.zhang.jie.test.api;

public class TestMain {
	
	public static void main(String[] args) throws Exception {
//		m1();		//创建一个线程
//		m2();		//线程运行的随机性
//		m3();		//start()方法的调用顺序和线程的执行顺序无关
//		m4();		//不共享实例数据
//		m5();		//共享实例数据
//		m6();		//承m5,加入同步处理
		m7();		//线程不安全问题（解决案例）
//		m8();		//Thread.currentThread()
//		m9();		//不要使用 this.getName() 方法
//		m10();		//isAlive()方法
//		m11();		//this.isAlive()和 Thread.currentThread().isAlive() 的区别
		m12();
	}

	
	/**
	 * getId(),获取线程的唯一ID
	 */
	public static void m12() {
		class A extends Thread {
			public void run() {
				System.out.println("this.id:" + this.getId()); // 三个10
				System.out.println("current.id:" + Thread.currentThread().getId()); // 分别是11,12,13
			}
		}
		A a = new A();
		Thread t1 = new Thread(a, "t1");
		Thread t2 = new Thread(a, "t2");
		Thread t3 = new Thread(a, "t3");
		t1.start();
		t2.start();
		t3.start();
	}
	
	/**
	 * isAlive()方法，this.isAlive()和 Thread.currentThread().isAlive() 的区别
	 * 建议使用 Thread.currentThread().isAlive() 
	 */
	public static void m11() {
		class A extends Thread {
			public A() {
				System.out.println("构造器中this.getName:" + this.getName()); // Thread-0，这个线程指的是A
				System.out.println("构造器中this.isAlive:" + this.isAlive()); // false，这里的线程指的是A，所以是false
				System.out.println("构造器中currentThread:" + Thread.currentThread().getName()); // main
				System.out.println("构造器中currentThread:" + Thread.currentThread().isAlive()); // true，这里的线程指的是main,所以是true
			}

			public void run() {
				System.out.println("run中this.getName:" + this.getName()); // Thread-0
				System.out.println("run中this.isAlive:" + this.isAlive()); // false，难道说在t中执行A里面的线程方法，就是false吗? 如果直接是new
																			// A().start()的话，这里是true
				System.out.println("run中currentThread:" + Thread.currentThread().getName()); // ttt
				System.out.println("run中currentThread:" + Thread.currentThread().isAlive()); // true，这是包装线程A的线程t
			}
		}
		Thread t = new Thread(new A(), "ttt");
		t.start();
	//   A a = new A();
	//   a.start();
	}
	
	/**
	 * isAlive()方法，判断线程是否处于活动状态（包括就绪状态、运行状态）
	 * @throws Exception 
	 */
	public static void m10() throws Exception {
		class A extends Thread {
			public void run() {
				try {
					System.out.println("is Alive:" + Thread.currentThread().isAlive());	//true
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		A a = new A();
		System.out.println("before begin," + a.isAlive());	//false
		a.start();
//		Thread.sleep(200);
		System.out.println("running ," + a.isAlive());	//true
		Thread.sleep(2000);
		System.out.println("after begin," + a.isAlive());	//false
	}
	
	/**
	 * 总结，不要使用 this.getName()
	 */
	public static void m9() {
		class A extends Thread{
			public A() {
				System.out.println("A() : "+this.getName());
				System.out.println("A() : "+Thread.currentThread().getName());
			}
			public void run() {
				System.out.println("run() : "+this.getName());
				System.out.println("run() : "+Thread.currentThread().getName());
			}
		}
		A a = new A();
		a.setName("ttt");
		a.start();
		
//		Thread t = new Thread(new A(), "ttt");
//		t.start();
	}
	
	/**
	 * Thread.currentThread()，指的是执行方法的所在线程
	 */
	public static void m8() {
		class A extends Thread{
			public A () {
				System.out.println("A() : "+Thread.currentThread().getName());		//main
			}
			public void run() {
				System.out.println("run() : "+Thread.currentThread().getName());	//thread-0
			}
		}
		A a = new A();
		a.start();
	}
	
	/**
	 *暂略
	 */
	private static void m7() {
		
	}

	/**
	 * 承m5(),加入同步处理,同一时间只能有一个线程在处理
	 */
	public static void m6() {
		class A extends Thread{
			private int counter = 5;
			synchronized public void run() {
				while(counter > 0) {
					System.out.println(Thread.currentThread().getName()+":"+counter);
					if(Thread.currentThread().getName().equals("A") && counter == 3) break;
					counter --;
				}
			}
		}
		A a = new A();
		Thread t1 = new Thread(a,"A");
		Thread t2 = new Thread(a,"B");
		Thread t3 = new Thread(a,"C");
		t1.start();
		t2.start();
		t3.start();
	}
	
	/**
	 * 共享实例数据的情况，多线程会同时访问同一个实例变量，如果没有同步处理，会有读数据不准确的问题
	 */
	public static void m5() {
		class A extends Thread{
			private int counter = 5;
			public void run() {
				while(counter > 0) {
					System.out.println(Thread.currentThread().getName()+":"+counter);
					counter --;
				}
			}
		}
		A a = new A();
		Thread t1 = new Thread(a,"A");
		Thread t2 = new Thread(a,"B");
		Thread t3 = new Thread(a,"C");
		t1.start();
		t2.start();
		t3.start();
	}

	/**
	 * 不共享实例数据的情况（多线程间没有影响）
	 */
	public static void m4() {
		class A extends Thread{
			private int counter = 5;
			public void run() {
				while(counter > 0) {
					System.out.println(Thread.currentThread().getName()+":"+counter);
					counter --;
				}
			}
		}
		A a1 = new A();
		A a2 = new A();
		A a3 = new A();
		
		a1.start();
		a2.start();
		a3.start();
	}
	
	/**
	 * start()方法的调用顺序和线程的执行顺序无关
	 */
	public static void m3() {
		class A extends Thread{
			private int i;
			public A(int i) {
				this.i = i;
			}
			public void run() {
				System.out.println(i);
			}
		}
		for(int i = 0;i < 10;i++) {
			A a = new A(i);
			a.start();
		}
	}
	
	/**
	 * 线程运行的随机性
	 */
	public static void m2() {
		class A extends Thread{
			public void run() {
				try {
					for(int i =0;i<10;i++) {
						int time = (int) (Math.random()*1000);
						Thread.sleep(time);
						System.out.println("thread name : "+Thread.currentThread().getName()+" ,value is : "+i);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		class B extends Thread{
			public void run() {
				try {
					for(int i =0;i<10;i++) {
						int time = (int) (Math.random()*1000);
						Thread.sleep(time);
						System.out.println("thread name : "+Thread.currentThread().getName()+" ,value is : "+i);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		A a = new A();
		B b = new B();
		a.start();
		b.start();
	}
	
	/**
	 * 创建一个线程
	 */
	public static void m1() {
		class A extends Thread{
			public void run() {
				System.out.println("A..");
			}
		}
		A a = new A();
		a.start();
		
		class B implements Runnable{
			@Override
			public void run() {
				System.out.println("B");
			}
		}
		//这里可能存在一些内外关系，未测
		new Thread(new B()).start();
	}
}
