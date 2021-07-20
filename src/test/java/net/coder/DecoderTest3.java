package net.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import junit.framework.TestCase;
import net.message.TankStopMovingMessage;
import net.message.type.MessageType;

import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/20 15:40
 */
public class DecoderTest3 extends TestCase {

    public void testDecode() {
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new Decoder());

        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(MessageType.TankStopMovingMessage.ordinal());
        buffer.writeInt(24); // length
        UUID uuid = UUID.randomUUID();       // id
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
        buffer.writeInt(5); // x
        buffer.writeInt(8); // y


        channel.writeInbound(buffer);

        TankStopMovingMessage tankStopMovingMessage = channel.readInbound();

        assertEquals(uuid, tankStopMovingMessage.getId());
        assertEquals(5, tankStopMovingMessage.getX());
        assertEquals(8, tankStopMovingMessage.getY());

    }
}