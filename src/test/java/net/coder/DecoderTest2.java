package net.coder;

import client.constant.Direction;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import junit.framework.TestCase;
import net.message.TankStartMovingMessage;
import net.message.type.MessageType;

import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/20 13:30
 */
public class DecoderTest2 extends TestCase {

    public void testDecode() {
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new Decoder());

        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(MessageType.TankMovingMessage.ordinal());
        buffer.writeInt(28); // length
        UUID uuid = UUID.randomUUID();       // id
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
        buffer.writeInt(5); // x
        buffer.writeInt(8); // y
        buffer.writeInt(Direction.Left.ordinal());  // direction


        channel.writeInbound(buffer);

        TankStartMovingMessage tankMovingMessage = channel.readInbound();

        assertEquals(uuid, tankMovingMessage.getId());
        assertEquals(5, tankMovingMessage.getX());
        assertEquals(8, tankMovingMessage.getY());
        assertEquals(Direction.Left, tankMovingMessage.getDirection());

    }
}