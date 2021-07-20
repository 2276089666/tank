package net.coder;

import client.constant.Direction;
import client.constant.Group;
import client.entity.PlayerTank;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import junit.framework.TestCase;
import net.message.TankJoinMessage;

import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/19 14:46
 */
public class TankJoinMessageEncoderTest extends TestCase {

    // 测试编码器Encoder
    public void testEncode() {
        EmbeddedChannel channel = new EmbeddedChannel();

        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new TankJoinMessageEncoder());

        PlayerTank playerTank = new PlayerTank(100, 200, Direction.Left, Group.Good);
        TankJoinMessage message = new TankJoinMessage(playerTank);
        channel.writeOutbound(message);  // 写出

        ByteBuf byteBuf = channel.readOutbound(); // 读入

        int length = byteBuf.readInt();
        int x = byteBuf.readInt();
        int y = byteBuf.readInt();
        Direction dir=Direction.values()[byteBuf.readInt()];
        boolean moving = byteBuf.readBoolean();
        Group group=Group.values()[byteBuf.readInt()];
        long mostSigBits = byteBuf.readLong();
        long leastSigBits = byteBuf.readLong();
        UUID uuid = new UUID(mostSigBits, leastSigBits);

        // 4+4+4+1+4+8+8+1=34
        assertEquals(34,length);
        assertEquals(playerTank.getX(),x);
        assertEquals(playerTank.getY(),y);
        assertEquals(playerTank.getDir(),dir);
        assertEquals(playerTank.isMoving(),moving);
        assertEquals(playerTank.getGroup(),group);
        assertEquals(playerTank.getId(),uuid);

    }
}