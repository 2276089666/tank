package entity;

import ResourceManager.ResourceManager;
import constant.Direction;
import constant.Group;
import frame.TankFrame;

import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * @Author ws
 * @Date 2021/7/14 19:57
 */
public class Tank {
    private int x;
    private int y;
    private Direction dir;
    private boolean left, right, up, down;
    private boolean moving = false;
    private Group group;

    public static final int speed = 10;

    public Tank(int x, int y, Direction dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
    }

    // 画tank
    public void fillRect(Graphics g) {
        if (this.group == Group.Good) {
            switch (dir) {
                case Right:
                    g.drawImage(ResourceManager.goodTankR, x, y, null);
                    break;
                case Left:
                    g.drawImage(ResourceManager.goodTankL, x, y, null);
                    break;
                case Up:
                    g.drawImage(ResourceManager.goodTankU, x, y, null);
                    break;
                case Down:
                    g.drawImage(ResourceManager.goodTankD, x, y, null);
                    break;
            }
        }
        if (this.group == Group.Bad) {
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
        }
        moveByDirection();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
        }
        setDirection();
    }

    private void setDirection() {
        if (!left && !right && !up && !down) {
            this.moving = false;
        } else {
            this.moving = true;
            if (left && !right && !up && !down) {
                dir = Direction.Left;
            }
            if (!left && right && !up && !down) {
                dir = Direction.Right;
            }
            if (!left && !right && up && !down) {
                dir = Direction.Up;
            }
            if (!left && !right && !up && down) {
                dir = Direction.Down;
            }
        }

    }

    public void moveByDirection() {
        if (!moving) return;
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

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_CONTROL:
                fire();
                break;
        }
        setDirection();
    }

    private void fire() {
        Bullet bullet = new Bullet(x, y, dir, group);
        TankFrame.getInstance().addBullet(bullet);
    }
}
