package net.message;

import client.constant.Direction;
import client.constant.Group;
import client.entity.Bullet;
import client.frame.TankFrame;
import net.message.type.MessageType;

import java.io.*;
import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/20 16:46
 */
public class BulletNewMessage extends AbstractMessage {

    private UUID id;
    private UUID playerId;
    private int x, y;
    private Direction direction;
    private Group group;

    public BulletNewMessage() {
    }

    public BulletNewMessage(Bullet bullet) {
        this.id = bullet.getId();
        this.playerId = bullet.getPlayerId();
        this.x = bullet.getX();
        this.y = bullet.getY();
        this.direction = bullet.getDir();
        this.group = Group.Bad;
    }

    @Override
    public byte[] objectToByteArr() {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        DataOutputStream dos = null;

        try {
            bos = new ByteArrayOutputStream();
            dos = new DataOutputStream(bos);
            // 先写高64位
            dos.writeLong(id.getMostSignificantBits());
            // 再写低64位
            dos.writeLong(id.getLeastSignificantBits());
            // 先写高64位
            dos.writeLong(playerId.getMostSignificantBits());
            // 再写低64位
            dos.writeLong(playerId.getLeastSignificantBits());
            dos.writeInt(x);
            dos.writeInt(y);
            // 枚举的下标
            dos.writeInt(direction.ordinal());
            dos.writeInt(group.ordinal());
            dos.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) dos.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public void parseByteArrToObject(byte[] data) {
        ByteArrayInputStream bis = null;
        DataInputStream dis = null;
        try {
            bis = new ByteArrayInputStream(data);
            dis = new DataInputStream(bis);
            this.id = new UUID(dis.readLong(), dis.readLong());
            this.playerId = new UUID(dis.readLong(), dis.readLong());
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.direction = Direction.values()[dis.readInt()];
            this.group=Group.values()[dis.readInt()];
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) dis.close();
                if (bis != null) bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle() {
        // 自己的playerTank发的消息,不处理
        if (this.playerId.equals(TankFrame.getInstance().getGameModel().getPlayerTank().getId())) return;

        Bullet bullet = new Bullet(this.x, this.y, this.direction, this.group, this.playerId);
        bullet.setId(this.id);
        TankFrame.getInstance().getGameModel().add(bullet);
    }

    @Override
    public int getType() {
        return MessageType.BulletNewMessage.ordinal();
    }

    @Override
    public String toString() {
        return "BulletNewMessage{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                ", group=" + group +
                '}';
    }
}
