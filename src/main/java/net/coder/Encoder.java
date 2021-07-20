package net.coder;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.message.AbstractMessage;

/**
 * @Author ws
 * @Date 2021/7/19 14:24
 */
public class Encoder extends MessageToByteEncoder<AbstractMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractMessage msg, ByteBuf out) throws Exception {
        // 1.写出message类型
        out.writeInt(msg.getType());
        byte[] bytes = msg.objectToByteArr();
        // 2.写出message字节数组长度
        out.writeInt(bytes.length);
        // 3.写出message数据
        out.writeBytes(bytes);
    }
}
