package netty.simpleNetty.future_promise;

import lombok.extern.slf4j.Slf4j;

import javax.print.DocFlavor;
import java.util.concurrent.*;


/**
 * test JDK Future
 */
@Slf4j
public class JDK_Future {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        第一种写法
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        // 创建一个callable任务
//        Callable<Integer> task = ()->{
//            // 模拟长时间运行的任务
//            Thread.sleep(2000);
//            return 20;
//        };
//        log.info("Main Thread : {}", Thread.currentThread().getName());
//        // 提交任务并获取Future
//        Future<Integer> future = executorService.submit(task);
//        // 在其他线程中获取Future执行的结果
//        new Thread(()->{
//            try {
//                Integer result = future.get();
//                log.info("Thread is {}, Get Future result:{}",Thread.currentThread().getName(), result);
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }).start();


        // 第二种实现方式
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 递交线程执行任务
        Future<?> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.info("current thread : {}", Thread.currentThread().getName());
                Thread.sleep(2000);
                return 20;
            }
        });
        log.info("Get future thread : {}, res : {}", Thread.currentThread().getName(), future.get());
    }
}
