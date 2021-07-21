package client.entity;


import client.ResourceManager.ResourceManager;
import client.constant.Direction;
import client.constant.Group;
import client.frame.TankFrame;
import net.client.NetClient;
import net.message.TankDieMessage;


import java.awt.*;
import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/15 9:55
 */
// 子弹
public class Bullet extends AbstractGameObject {
    private static final int speed = 20;
    private Rectangle rectangle = null;
    private int x, y;
    private Direction dir;
    private Group group;
    private boolean isAlive = true; // 子弹越界,就表示子弹死亡
    private UUID id;
    private UUID playerId;

    public Bullet(int x, int y, Direction dir, Group group,UUID playerId) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.rectangle = new Rectangle();
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = ResourceManager.bulletD.getWidth();
        this.rectangle.height = ResourceManager.bulletD.getHeight();
        this.id= UUID.randomUUID();
        this.playerId=playerId;
    }

    // 画子弹
    public void fillRect(Graphics g) {
        switch (dir) {
            case Right:
                g.drawImage(this.group == Group.ManMachine ? ResourceManager.bulletR : ResourceManager.goodBulletR, x, y, null);
                break;
            case Left:
                g.drawImage(this.group == Group.ManMachine ? ResourceManager.bulletL : ResourceManager.goodBulletL, x, y, null);
                break;
            case Up:
                g.drawImage(this.group == Group.ManMachine ? ResourceManager.bulletU : ResourceManager.goodBulletU, x, y, null);
                break;
            case Down:
                g.drawImage(this.group == Group.ManMachine ? ResourceManager.bulletD : ResourceManager.goodBulletD, x, y, null);
                break;
        }
        moveByDirection();
        this.rectangle.x = x;
        this.rectangle.y = y;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Direction getDir() {
        return dir;
    }

    public Group getGroup() {
        return group;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    private void moveByDirection() {
        switch (dir) {
            case Up:
                y -= speed;
                break;
            case Down:
                y += speed;
                break;
            case Left:
                x -= speed;
                break;
            case Right:
                x += speed;
                break;
        }
        checkOutOfBounds();
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean collisionWithTank(Tank tank) {
        // 避免tank死了,其坐标还在,会导致我们的子弹到死亡坦克之前的坐标会消失
        if (!tank.isAlive()) return true;
        // 避免一个子弹打死多个tank
        if (!this.isAlive()) return true;
        // 同队不攻击
        if (this.group == tank.getGroup()) return true;
        Rectangle rectangle = this.getRectangle();
        Rectangle rectangle2 = tank.getRectangle();
        // 两个矩阵有相交,说明子弹打中了,对应子弹和坦克die
        if (rectangle.intersects(rectangle2)) {
            this.die();
            tank.die();
            return true;
        }
        return false;
    }

    public void die() {
        isAlive = false;
    }

    private void checkOutOfBounds() {
        if (x < 0 || x > TankFrame.GAME_WIDTH || y < 30 || y > TankFrame.GAME_HEIGHT) {
            isAlive = false;
        }
    }

    public boolean collisionWithWall(Wall wall) {
        if (!this.isAlive()) return true;
        Rectangle rectangle = this.getRectangle();
        Rectangle wallRectangle = wall.getRectangle();
        if (rectangle.intersects(wallRectangle)) {
            this.die();
            return true;
        }
        return false;
    }

    public boolean collisionWithPlayerTank(PlayerTank playerTank) {
        if (!this.isAlive()) return true;
        if (!playerTank.isAlive()) return true;
        // 自己发出的子弹不能打自己
        if (this.group == playerTank.getGroup()) return false;
        Rectangle rectangle = this.getRectangle();
        Rectangle playerTankRectangle = playerTank.getRectangle();
        if (rectangle.intersects(playerTankRectangle)) {
            this.die();
            playerTank.die();
            NetClient.getInstance().send(new TankDieMessage(this.getId(),playerTank.getId()));
            return true;
        }
        return false;
    }
}
