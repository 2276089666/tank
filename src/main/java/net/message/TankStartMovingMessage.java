package net.message;

import client.constant.Direction;
import client.entity.PlayerTank;
import client.frame.TankFrame;
import net.message.type.MessageType;

import java.io.*;
import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/20 11:48
 */
public class TankStartMovingMessage extends AbstractMessage {

    private UUID id;
    private int x, y;
    private Direction direction;

    public TankStartMovingMessage() {
    }

    public TankStartMovingMessage(UUID id, int x, int y, Direction direction) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public UUID getId() {
        return id;
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
            dos.writeInt(x);
            dos.writeInt(y);
            // 枚举的下标
            dos.writeInt(direction.ordinal());
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
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.direction = Direction.values()[dis.readInt()];
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
        // 如果是自己的消息,不处理
        if (this.getId().equals(TankFrame.getInstance().getGameModel().getPlayerTank().getId())) return;

        PlayerTank playerTank = TankFrame.getInstance().getGameModel().getPlayerTankByUUID(this.id);

        if (playerTank!=null){
            playerTank.setX(this.x);
            playerTank.setY(this.y);
            playerTank.setDir(this.direction);
            playerTank.setMoving(true);
        }

    }

    @Override
    public int getType() {
        return MessageType.TankMovingMessage.ordinal();
    }

    @Override
    public String toString() {
        return "TankMovingMessage{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                '}';
    }
}
