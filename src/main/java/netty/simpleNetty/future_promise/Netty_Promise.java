package netty.simpleNetty.future_promise;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * Netty Promise Test
 */
@Slf4j
public class Netty_Promise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop eventLoop = new NioEventLoopGroup().next();
        // 主动创建promise，结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);
        log.info("task begin ...");
        new Thread(()->{
            log.info("task is running...");
            // 任意一个线程执行计算，计算完毕后向promise中填充结果。
            try {
//                int i = 1 / 0;
                Thread.sleep(1000);
                promise.setSuccess(10);
            } catch (Exception e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
            log.info("task end ...");
        }).start();
        log.info("等待结果...");
        log.info("获取任务执行结果：{}", promise.get());
    }
}
