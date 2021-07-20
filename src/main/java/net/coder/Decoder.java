package net.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.message.*;
import net.message.type.MessageType;

import java.util.List;

/**
 * @Author ws
 * @Date 2021/7/19 18:15
 */
public class Decoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1.小于消息头的长度
        if (in.readableBytes() < 8) return;

        // 2.记录读指针
        in.markReaderIndex();

        // 3.读消息类型
        MessageType type = MessageType.values()[in.readInt()];

        // 4.读消息长度
        int length = in.readInt();

        // 5.如果消息被粘包拆包,buf里面的数据,不够一整个对象,读指针回退到之前记录的位置
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[length];
        in.readBytes(data);

        AbstractMessage abstractMessage = null;
        switch (type) {
            case TankJoinMessage:
                abstractMessage = new TankJoinMessage();
                break;
            case TankMovingMessage:
                abstractMessage = new TankStartMovingMessage();
                break;
            case TankStopMovingMessage:
                abstractMessage = new TankStopMovingMessage();
                break;
            case TankChangeDirMessage:
                abstractMessage = new TankChangeDirMessage();
                break;
            case BulletNewMessage:
                abstractMessage = new BulletNewMessage();
                break;
            case TankDieMessage:
                abstractMessage=new TankDieMessage();
                break;
        }
        abstractMessage.parseByteArrToObject(data);
        out.add(abstractMessage);
    }
}
