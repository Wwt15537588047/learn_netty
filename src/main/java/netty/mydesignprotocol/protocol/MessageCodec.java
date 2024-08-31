package netty.mydesignprotocol.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import netty.mydesignprotocol.message.Message;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {
    // 编码器
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 1. 4字节的魔术
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1字节的版本
        out.writeByte(1);
        // 3. 1字节的序列化方式 jdk 0, json 1
        out.writeByte(0);
        // 4. 1字节的指令类型
        out.writeByte(msg.getMessageType());
        // 5. 4字节的序列号
        out.writeInt(msg.getSequenceId());
        // 无意义，对其填充，使其前缀保持16字节
        out.writeByte(0xff);
        // 6. 获取内容的字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();
        // 7.长度
        out.writeInt(bytes.length);
        // 8. 写入内容
        out.writeBytes(bytes);
    }

    // 解码器
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1. 获取魔术
        int magicNum = in.readInt();
        // 2. 1字节的版本
        byte version = in.readByte();
        // 3. 1字节的序列化方式 jdk 0, json 1
        byte serializerType = in.readByte();
        // 4. 1字节的指令类型
        byte messageType = in.readByte();
        // 5. 4字节的序列号
        int sequenceId = in.readInt();
        // 无意义，对其填充
        in.readByte();
        // 读取长度
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Message message = (Message) ois.readObject();
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", message);
        out.add(message);
    }
}
