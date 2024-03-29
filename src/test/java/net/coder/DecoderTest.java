package net.coder;

import client.constant.Direction;
import client.constant.Group;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import junit.framework.TestCase;
import net.message.TankJoinMessage;
import net.message.type.MessageType;

import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/19 18:38
 */
public class DecoderTest extends TestCase {

    public void testDecode() {
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new Decoder());

        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(MessageType.TankJoinMessage.ordinal());
        buffer.writeInt(32); // length
        buffer.writeInt(5); // x
        buffer.writeInt(8); // y
        buffer.writeInt(Direction.Left.ordinal());  // direction
        buffer.writeInt(Group.Good.ordinal()); // group
        UUID uuid = UUID.randomUUID();       // id
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
        buffer.writeBoolean(false);
        buffer.writeBoolean(true);

        channel.writeInbound(buffer);

        TankJoinMessage tankJoinMessage = channel.readInbound();

        assertEquals(5, tankJoinMessage.getX());
        assertEquals(8, tankJoinMessage.getY());
        assertEquals(Direction.Left, tankJoinMessage.getDirection());
        assertEquals(Group.Good, tankJoinMessage.getGroup());
        assertEquals(uuid, tankJoinMessage.getUuid());
    }
}