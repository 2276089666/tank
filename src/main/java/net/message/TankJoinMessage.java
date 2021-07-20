package net.message;

import client.constant.Direction;
import client.constant.Group;
import client.entity.PlayerTank;
import client.entity.Tank;
import client.frame.TankFrame;
import net.client.NetClient;
import net.server.NetServer;

import java.io.*;
import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/19 13:03
 */
public class TankJoinMessage {
    private int x, y;
    private Direction direction;
    private boolean moving;
    private Group group;
    private UUID uuid;
    private boolean fire;

    public TankJoinMessage(PlayerTank playerTank) {
        this.x = playerTank.getX();
        this.y = playerTank.getY();
        this.direction = playerTank.getDir();
        this.moving = playerTank.isMoving();
        this.group = playerTank.getGroup();
        this.uuid = playerTank.getId();
        this.fire=playerTank.isFire();
    }

    public TankJoinMessage() {

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isMoving() {
        return moving;
    }

    public Group getGroup() {
        return group;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isFire() {
        return fire;
    }

    public byte[] objectToByteArr() {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        DataOutputStream dos = null;

        try {
            bos = new ByteArrayOutputStream();
            dos = new DataOutputStream(bos);

            dos.writeInt(x);
            dos.writeInt(y);
            // 枚举的下标
            dos.writeInt(direction.ordinal());
            dos.writeBoolean(moving);
            dos.writeInt(group.ordinal());
            // UUID是两个long组成的串
            // 先写高64位
            dos.writeLong(uuid.getMostSignificantBits());
            // 再写低64位
            dos.writeLong(uuid.getLeastSignificantBits());
            dos.writeBoolean(fire);
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

    public void parseByteArrToObject(byte[] data) {
        ByteArrayInputStream bis = null;
        DataInputStream dis = null;
        try {
            bis = new ByteArrayInputStream(data);
            dis = new DataInputStream(bis);
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.direction = Direction.values()[dis.readInt()];
            this.moving = dis.readBoolean();
            this.group = Group.values()[dis.readInt()];
            this.uuid = new UUID(dis.readLong(), dis.readLong());
            this.fire=dis.readBoolean();
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


    public void handle() {
        // 如果是自己的消息,不处理
        if (this.getUuid().equals(TankFrame.getInstance().getGameModel().getPlayerTank().getId())) return;
        // 否则,判断新玩家是否已经存在,存在则更新新玩家playerTank位置,否则加入新的玩家tank
        PlayerTank playerTank = new PlayerTank(this);
        TankFrame.getInstance().getGameModel().updateOrInsertByUUID(playerTank);

        // 避免后来的client没有前面先到的client的信息,在此再转发一次
        NetClient.getInstance().send(new TankJoinMessage(TankFrame.getInstance().getGameModel().getPlayerTank()));
    }

    @Override
    public String toString() {
        return "TankJoinMessage{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                ", moving=" + moving +
                ", group=" + group +
                ", uuid=" + uuid +
                ", fire=" + fire +
                '}';
    }
}
