package entity;

import ResourceManager.ResourceManager;
import constant.Direction;
import constant.Group;
import frame.TankFrame;

import java.awt.*;
import java.util.Random;


/**
 * @Author ws
 * @Date 2021/7/14 19:57
 */
// 电脑
public class Tank {
    public static final int speed = 5;
    private int x;
    private int y;
    private Direction dir;
    private boolean left, right, up, down;

    private int preX;
    private int preY;

    public Group getGroup() {
        return group;
    }

    private Group group;
    public boolean isAlive = true;

    public Tank(int x, int y, Direction dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.preX = x;
        this.preY = y;
    }

    // 画tank
    public void fillRect(Graphics g) {
        if (!this.isAlive) return;
        switch (dir) {
            case Right:
                g.drawImage(ResourceManager.badTankR, x, y, null);
                break;
            case Left:
                g.drawImage(ResourceManager.badTankL, x, y, null);
                break;
            case Up:
                g.drawImage(ResourceManager.badTankU, x, y, null);
                break;
            case Down:
                g.drawImage(ResourceManager.badTankD, x, y, null);
                break;
        }
        moveByDirection();
    }

    public void moveByDirection() {
        preX = x;
        preY = y;
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
        setRandomDirection();
        fire();
    }

    private void checkOutOfBounds() {
        if (x < 0 || x > TankFrame.GAME_WIDTH - ResourceManager.badTankD.getWidth() || y < 30 || y > TankFrame.GAME_HEIGHT - ResourceManager.badTankD.getHeight()) {
            this.back();
        }
    }

    // tank即将出界,让tank回到出界前的位置
    private void back() {
        x = preX;
        y = preY;
    }

    Random r = new Random();

    private void setRandomDirection() {
        // 换方向太频繁,控制频率
        if (r.nextInt(100) > 80) {
            this.dir = Direction.getRandomDirection();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private void fire() {
        // 发射子弹太频繁,控制频率
        if (r.nextInt(100) > 80) {
            int bx = x + ResourceManager.goodTankU.getWidth() / 2 - ResourceManager.bulletU.getWidth() / 2;
            int by = y + ResourceManager.goodTankU.getHeight() / 2 - ResourceManager.bulletU.getHeight() / 2;
            Bullet bullet = new Bullet(bx, by, dir, group);
            TankFrame.getInstance().addBullet(bullet);
        }
    }

    public void die() {
        this.isAlive = false;
        TankFrame.getInstance().explosions.add(new Explosion(x, y));
    }
}
