package net.message;

import client.entity.Bullet;
import client.entity.PlayerTank;
import client.frame.TankFrame;
import net.message.type.MessageType;

import java.io.*;
import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/20 19:21
 */
public class TankDieMessage extends AbstractMessage {

    private UUID bulletId;
    private UUID playerTankId;

    public TankDieMessage() {
    }

    public TankDieMessage(UUID bulletId, UUID playerTankId) {
        this.bulletId = bulletId;
        this.playerTankId = playerTankId;
    }

    public UUID getBulletId() {
        return bulletId;
    }

    public void setBulletId(UUID bulletId) {
        this.bulletId = bulletId;
    }

    public UUID getPlayerTankId() {
        return playerTankId;
    }

    public void setPlayerTankId(UUID playerTankId) {
        this.playerTankId = playerTankId;
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
            dos.writeLong(this.bulletId.getMostSignificantBits());
            // 再写低64位
            dos.writeLong(this.bulletId.getLeastSignificantBits());
            // 先写高64位
            dos.writeLong(this.playerTankId.getMostSignificantBits());
            // 再写低64位
            dos.writeLong(this.playerTankId.getLeastSignificantBits());
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
            this.bulletId = new UUID(dis.readLong(), dis.readLong());
            this.playerTankId = new UUID(dis.readLong(), dis.readLong());
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
        Bullet bullet = TankFrame.getInstance().getGameModel().getBulletById(this.bulletId);
        if (bullet != null) {
            bullet.die();
        }

        PlayerTank playerTank = TankFrame.getInstance().getGameModel().getPlayerTankByUUID(this.playerTankId);
        if (this.playerTankId.equals(TankFrame.getInstance().getGameModel().getPlayerTank().getId())) {
            TankFrame.getInstance().getGameModel().getPlayerTank().die();
        } else {
            if (playerTank != null) {
                playerTank.die();
            }
        }
    }

    @Override
    public int getType() {
        return MessageType.TankDieMessage.ordinal();
    }

    @Override
    public String toString() {
        return "TankDieMessage{" +
                "bulletId=" + bulletId +
                ", playerTankId=" + playerTankId +
                '}';
    }
}
