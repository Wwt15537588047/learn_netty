package netty.netProgram.selector;

import lombok.extern.slf4j.Slf4j;
import netty.utils.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class ServerAcceptReadWrite {
    public static void main(String[] args) {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(8080));
            Selector selector = Selector.open();
            // 注册ssc到selector上,并且注明事件类型为ACCEPT事件
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true){
                int count = selector.select();
                if(count <= 0){
                    continue;
                }
                log.debug("current connect num : {}", count);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectionKeys.iterator();
                while (iter.hasNext()){
                    SelectionKey key = iter.next();
                    // 判断事件类型是accept还是read
                    if(key.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel sc = serverSocketChannel.accept();
                        log.debug("ssc is: {}, sc is:{}", serverSocketChannel, sc);
                        sc.configureBlocking(false);
                        // 将sc注册到selector上,并设置其类型为Read
                        SelectionKey scKey = sc.register(selector, SelectionKey.OP_READ);

                        // 向client写入数据
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 10000000; i++) {
                            sb.append("a");
                        }
                        ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                        int write = sc.write(buffer);
                        log.debug("actual write bytes:{}", write);
                        if(buffer.hasRemaining()){
                            // 如果此时没有将buffer中的所有数据写入到channel,scKey在原有Read事件的基础上添加Write事件
                            scKey.interestOps(scKey.interestOps() + SelectionKey.OP_WRITE);
                            // 同时需要将buffer作为附件关联到scKey上
                            scKey.attach(buffer);
                        }
                    }else if(key.isReadable()){
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(128);
                        int read = sc.read(buffer);
                        if(read == -1){
                            // 此时client已经关闭连接
                            // key.cancel()取消注册在selector上的channel,并从keys集合中删除key后续不在监听事件
                            key.cancel();
                            sc.close();
                        }else{
                            buffer.flip();
                            ByteBufferUtil.debugAll(buffer);
                        }
                    }else if(key.isWritable()){
                        ByteBuffer buffer  = (ByteBuffer) key.attachment();
                        SocketChannel channel = (SocketChannel) key.channel();
                        int write = channel.write(buffer);
                        log.debug("hit write, current write bytes:{}", write);
                        if(!buffer.hasRemaining()){
                            // 只要向 channel 发送数据时，socket 缓冲可写，这个事件会频繁触发，
                            // 因此应当只在 socket 缓冲区写不下时再关注可写事件，数据写完之后再取消关注
                            key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                            key.attach(null);
                        }
                    }
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
