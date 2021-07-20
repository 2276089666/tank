package net.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import junit.framework.TestCase;
import net.message.TankStopMovingMessage;
import net.message.type.MessageType;

import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/20 15:42
 */
public class EncoderTest3 extends TestCase {

    public void testEncode() {
        EmbeddedChannel channel = new EmbeddedChannel();

        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new Encoder());


        TankStopMovingMessage message = new TankStopMovingMessage(UUID.randomUUID(), 5, 8);
        channel.writeOutbound(message);  // 写出

        ByteBuf byteBuf = channel.readOutbound(); // 读入

        MessageType messageType=MessageType.values()[byteBuf.readInt()];
        int length = byteBuf.readInt();
        long mostSigBits = byteBuf.readLong();
        long leastSigBits = byteBuf.readLong();
        UUID uuid = new UUID(mostSigBits, leastSigBits);
        int x = byteBuf.readInt();
        int y = byteBuf.readInt();

        assertEquals(MessageType.TankStopMovingMessage,messageType);
        // 8+8+4+4=24
        assertEquals(24,length);
        assertEquals(message.getId(),uuid);
        assertEquals(message.getX(),x);
        assertEquals(message.getY(),y);
    }
}