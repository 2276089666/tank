package entity;

import ResourceManager.ResourceManager;
import constant.Direction;
import constant.Group;

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
    private static final int speed=10;

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
    }
}
