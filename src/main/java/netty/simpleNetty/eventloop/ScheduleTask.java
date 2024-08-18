package netty.simpleNetty.eventloop;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 使用NioEventLoop执行定时任务
 */
@Slf4j
public class ScheduleTask {
    public static void main(String[] args) {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(2);
        log.info("server start...");
        eventExecutors.scheduleAtFixedRate(()->{
            log.info("NioEventLoop is running schedule task, current time: {}", new Date());
        }, 0, 2, TimeUnit.SECONDS);
    }
}
