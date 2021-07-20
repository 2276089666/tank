package client.entity;

import client.ResourceManager.ResourceManager;
import client.constant.Direction;
import client.constant.Group;
import client.frame.TankFrame;
import client.model.GameModel;
import net.message.TankJoinMessage;

import java.awt.*;
import java.util.Random;
import java.util.UUID;


/**
 * @Author ws
 * @Date 2021/7/14 19:57
 */
// 电脑
public class Tank extends AbstractGameObject {
    public static final int speed = 5;
    private Random r = new Random();
    private int x;
    private int y;
    private Direction dir;
    private int preX;
    private int preY;
    private Group group;
    private boolean isAlive = true;
    private Rectangle rectangle = null;
    private boolean moving=true;
    private UUID id;
    private boolean fire=true;

    public Tank(int x, int y, Direction dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.preX = x;
        this.preY = y;
        this.rectangle = new Rectangle();
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = ResourceManager.badTankD.getWidth();
        this.rectangle.height = ResourceManager.badTankD.getHeight();
        this.id=UUID.randomUUID();
        this.fire=true;
    }



    public Group getGroup() {
        return group;
    }

    public UUID getId() {
        return id;
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
        this.rectangle.x = this.x;
        this.rectangle.y = this.y;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
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
        if (!fire) return;
        fire();
    }

    private void checkOutOfBounds() {
        if (x < 0 || x > TankFrame.GAME_WIDTH - ResourceManager.badTankD.getWidth() || y < 30 || y > TankFrame.GAME_HEIGHT - ResourceManager.badTankD.getHeight()) {
            this.back();
        }
    }

    // tank即将出界,让tank回到出界前的位置
    public void back() {
        x = preX;
        y = preY;
    }

    private void setRandomDirection() {
        // 换方向太频繁,控制频率
        if (r.nextInt(100) > 80) {
            this.dir = Direction.getRandomDirection();
        }
    }


    private void fire() {
        // 发射子弹太频繁,控制频率
        if (r.nextInt(100) > 80) {
            int bx = x + ResourceManager.goodTankU.getWidth() / 2 - ResourceManager.bulletU.getWidth() / 2;
            int by = y + ResourceManager.goodTankU.getHeight() / 2 - ResourceManager.bulletU.getHeight() / 2;
            Bullet bullet = new Bullet(bx, by, dir, group);
            GameModel.getInstance().add(bullet);
        }
    }

    public void die() {
        this.isAlive = false;
        GameModel.getInstance().add(new Explosion(x, y));
    }

    public boolean collisionWithWall(Wall wall) {
        if (!this.isAlive()) return true;
        Rectangle rectangle = this.getRectangle();
        Rectangle wallRectangle = wall.getRectangle();
        if (rectangle.intersects(wallRectangle)) {
            this.back();
            return false;
        }
        return false;
    }

    public boolean collisionWithTank(Tank tank2) {
        if (!this.isAlive()) return true;
        if (!tank2.isAlive()) return true;
        Rectangle rectangle = this.getRectangle();
        Rectangle rectangle1 = tank2.getRectangle();
        if (rectangle.intersects(rectangle1)) {
            this.back();
            tank2.back();
            return false;
        }
        return false;
    }
}
