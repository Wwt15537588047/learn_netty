package netty.netProgram.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class ClientWrite {
    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            SocketChannel sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
            sc.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8080));
            int count = 0;
            while (true){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectionKeys.iterator();
                while (iter.hasNext()){
                    SelectionKey key = iter.next();
                    iter.remove();
                    if(key.isConnectable()){
                        log.debug("client connect {}", sc.finishConnect());
                    }else if(key.isReadable()){
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        int read = socketChannel.read(buffer);
                        if(read >= 0){
                            count += read;
                            log.debug("current bytes total nums:{}",count);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
