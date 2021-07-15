package entity;

import ResourceManager.ResourceManager;
import constant.Direction;
import constant.Group;
import frame.TankFrame;

import java.awt.*;

/**
 * @Author ws
 * @Date 2021/7/15 9:55
 */
// 子弹
public class Bullet {
    private int x, y;
    private Direction dir;
    private Group group;
    private static final int speed = 20;
    private boolean isAlive = true; // 子弹越界,就表示子弹死亡

    public Bullet(int x, int y, Direction dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
    }

    // 画子弹
    public void fillRect(Graphics g) {
        switch (dir) {
            case Right:
                g.drawImage(ResourceManager.bulletR, x, y, null);
                break;
            case Left:
                g.drawImage(ResourceManager.bulletL, x, y, null);
                break;
            case Up:
                g.drawImage(ResourceManager.bulletU, x, y, null);
                break;
            case Down:
                g.drawImage(ResourceManager.bulletD, x, y, null);
                break;
        }
        moveByDirection();
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

    public void collisionWithTank(Tank tank){
        // 避免tank死了,其坐标还在,会导致我们的子弹到死亡坦克之前的坐标会消失
        if (!tank.isAlive) return;
        // 避免一个子弹打死多个tank
        if (!this.isAlive) return;
        // 同队不攻击
        if (this.group==tank.getGroup()) return;
        Rectangle bulletRectangle = new Rectangle(x, y, ResourceManager.bulletD.getWidth(), ResourceManager.bulletD.getHeight());
        Rectangle tankRectangle = new Rectangle(tank.getX(), tank.getY(), ResourceManager.badTankD.getWidth(), ResourceManager.badTankD.getHeight());
        // 两个矩阵有相交,说明子弹打中了,对应子弹和坦克die
        if (bulletRectangle.intersects(tankRectangle)){
            this.die();
            tank.die();
        }

    }

    private void die() {
        isAlive=false;
    }

    private void checkOutOfBounds() {
        if (x < 0 || x > TankFrame.GAME_WIDTH || y < 30 || y > TankFrame.GAME_HEIGHT) {
            isAlive = false;
        }
    }
}
