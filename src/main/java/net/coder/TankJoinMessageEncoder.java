package net.coder;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.message.TankJoinMessage;

/**
 * @Author ws
 * @Date 2021/7/19 14:24
 */
public class TankJoinMessageEncoder extends MessageToByteEncoder<TankJoinMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TankJoinMessage msg, ByteBuf out) throws Exception {
        byte[] bytes = msg.objectToByteArr();
        // message字节数组长度
        out.writeInt(bytes.length);
        // message数据
        out.writeBytes(bytes);
    }
}
