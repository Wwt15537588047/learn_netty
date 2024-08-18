package netty.simpleNetty.eventloop;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用NioEventLoop执行普通任务
 */
@Slf4j
public class NormalTask {
    public static void main(String[] args) {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(2);
        log.info("service start...");
        eventExecutors.execute(()->{
            log.info("normal task is running...");
        });

    }
}
