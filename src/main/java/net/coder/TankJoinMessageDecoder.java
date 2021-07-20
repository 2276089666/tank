package net.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.message.TankJoinMessage;

import java.util.List;

/**
 * @Author ws
 * @Date 2021/7/19 18:15
 */
public class TankJoinMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes()<38) return;

        int length = in.readInt();
        byte[] data = new byte[length];
        in.readBytes(data);

        TankJoinMessage tankJoinMessage = new TankJoinMessage();
        tankJoinMessage.parseByteArrToObject(data);

        out.add(tankJoinMessage);
    }
}
