package net.coder;

import client.constant.Direction;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import junit.framework.TestCase;
import net.message.TankStartMovingMessage;
import net.message.type.MessageType;

import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/20 13:34
 */
public class EncoderTest2 extends TestCase {

    public void testEncode() {
        EmbeddedChannel channel = new EmbeddedChannel();

        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new Encoder());


        TankStartMovingMessage message = new TankStartMovingMessage(UUID.randomUUID(), 5, 8, Direction.Left);
        channel.writeOutbound(message);  // 写出

        ByteBuf byteBuf = channel.readOutbound(); // 读入

        MessageType messageType=MessageType.values()[byteBuf.readInt()];
        int length = byteBuf.readInt();
        long mostSigBits = byteBuf.readLong();
        long leastSigBits = byteBuf.readLong();
        UUID uuid = new UUID(mostSigBits, leastSigBits);
        int x = byteBuf.readInt();
        int y = byteBuf.readInt();
        Direction dir=Direction.values()[byteBuf.readInt()];

        assertEquals(MessageType.TankMovingMessage,messageType);
        // 8+8+4+4+4=28
        assertEquals(28,length);
        assertEquals(message.getId(),uuid);
        assertEquals(message.getX(),x);
        assertEquals(message.getY(),y);
        assertEquals(message.getDirection(),dir);
    }
}