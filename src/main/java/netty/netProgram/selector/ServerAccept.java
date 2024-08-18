package netty.netProgram.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * selector处理accept事件Server
 */
@Slf4j
public class ServerAccept {
    public static void main(String[] args) {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(8080));
            // 创建selector
            Selector selector = Selector.open();
            // 绑定ssc到selector并指定当发生accept事件时触发
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true){
                // 获取发生事件的channel数量
                int count = selector.select();
                if(count <= 0){
                    continue;
                }
                log.debug("current connected nums : {}", count);
                // 获取所有发生连接的事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectionKeys.iterator();
                while (iter.hasNext()){
                    SelectionKey key = iter.next();
                    // 判断事件类型
                    if(key.isAcceptable()){
                        ServerSocketChannel sSC = (ServerSocketChannel) key.channel();
                        SocketChannel sc = sSC.accept();
                        log.debug("sc is :{}", sc);
                    }
                    // 及时移除处理好的事件
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
