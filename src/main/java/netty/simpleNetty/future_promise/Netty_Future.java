package netty.simpleNetty.future_promise;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Netty Future
 */
@Slf4j
public class Netty_Future {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoop eventLoop = eventLoopGroup.next();
        log.info("task begin ...");
        Future<Integer> future = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.info("task is running...");
                Thread.sleep(1000);
                // future是被动获取结果的，需要执行任务的线程向future中填充返回结果
                log.info("task is end ...");
                return 70;
            }
        });

        log.info("等待结果...");
        // 同步方式获取任务执行结果
        log.info("获取任务执行结果：{}", future.get());
        // 异步方式获取执行结果
/*        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                // 异步获取执行结果既可以用future.get()阻塞方法，也可以用future.getNow()非阻塞方法。
                // 异步既然执行到了回调函数这一步，肯定已经能够获取到执行结果。
                log.info("获取任务执行结果：{}", future.getNow());
            }
        });*/
    }
}
