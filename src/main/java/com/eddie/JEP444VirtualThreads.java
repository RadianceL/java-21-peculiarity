package com.eddie;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 在JDK19[<a href="https://openjdk.org/jeps/425">(JEP 425: Virtual Threads (Preview))作为第一次preview</a>]
 * 在JDK20JEP 436: Virtual Threads (Second Preview)作为第二次preview，此版本java.lang.ThreadGroup被永久废弃
 * 在JDK21版本，Virtual Threads正式发布，与之前版本相比，这次支持了ThreadLocal，然后也可以通过Thread.Builder来创建，而且也支持ThreadDump(jcmd <pid> Thread.dump_to_file -format=json <file>)
 * @author eddie.lys
 * @since 2023/10/7
 */
public class JEP444VirtualThreads {

    public static void main(String[] args) throws InterruptedException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var a = executor.submit(() -> System.out.println("url1"));
            var b = executor.submit(() -> System.out.println("url2"));
        }

        // 一般用Executors.newVirtualThreadPerTaskExecutor()是想通过池化技术来减少对象创建开销，
        // 不过由于虚拟线程相比平台线程更为"廉价"，因而不再需要池化，如果需要控制虚拟线程数则可以使用信号量的方式，
        // 因而提供了Thread.Builder来直接创建虚拟线程，示例如下
        Thread thread = Thread.ofVirtual().name("duke").unstarted(() -> System.out.println("url3"));
        thread.start();
        Thread.startVirtualThread(() -> System.out.println("Url4"));
        // 使用信号量控制线程数
        Semaphore semaphore = new Semaphore(2);
        for (int i = 5; i <= 10; i++) {
            if (semaphore.tryAcquire(1,10, TimeUnit.MINUTES)) {
                int finalI = i;
                Thread.startVirtualThread(() -> {
                            System.out.println(STR."semaphore -> Url\{finalI}");
                            semaphore.release();
                        }
                );
            }
        }
        TimeUnit.SECONDS.sleep(1L);
    }
}
