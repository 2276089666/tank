package client.entity;

import client.ResourceManager.ResourceManager;
import client.constant.Direction;
import client.constant.Group;
import client.frame.TankFrame;
import client.strategy.fire.FireStrategy;
import client.util.PropertiesUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author ws
 * @Date 2021/7/15 15:30
 */
// 玩家
public class PlayerTank extends AbstractGameObject {
    public static final int speed = 10;
    public boolean isAlive = true;
    private int x;
    private int y;
    private Direction dir;
    private boolean left, right, up, down;
    private boolean moving = false;
    private Group group;
    private Rectangle rectangle = null;
    private int preX;
    private int preY;

    public PlayerTank(int x, int y, Direction dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.rectangle = new Rectangle();
        this.preX = x;
        this.preY = y;
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width=ResourceManager.goodTankU.getWidth();
        this.rectangle.height=ResourceManager.goodTankU.getHeight();
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Direction getDir() {
        return dir;
    }

    public Group getGroup() {
        return group;
    }

    // 画tank
    public void fillRect(Graphics g) {
        if (!this.isAlive()) return;
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
        moveByDirection();
        this.rectangle.x = this.x;
        this.rectangle.y = this.y;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
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
        this.preX = this.x;
        this.preY = this.y;
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
        checkOutOfBounds();
    }

    private void checkOutOfBounds() {
        if (x < 0 || x > TankFrame.GAME_WIDTH - ResourceManager.badTankD.getWidth() || y < 30 || y > TankFrame.GAME_HEIGHT - ResourceManager.badTankD.getHeight()) {
            this.back();
        }
    }

    // tank即将出界,让tank回到出界前的位置
    private void back() {
        this.x = this.preX;
        this.y = this.preY;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
            case KeyEvent.VK_SPACE:
                fire();
                break;
        }
        setDirection();
    }

    private void fire() {
        try {
            String className = PropertiesUtil.get("fireStrategy");
            Class<?> aClass = Class.forName("client.strategy.fire." + className);
            FireStrategy fireStrategy = (FireStrategy) aClass.getDeclaredConstructor().newInstance();
            fireStrategy.fire(this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void die() {
        this.isAlive = false;
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

    public boolean collisionWithWall(Wall wall) {
        if (!this.isAlive()) return true;
        Rectangle rectangle = this.getRectangle();
        Rectangle wallRectangle = wall.getRectangle();
        if (rectangle.intersects(wallRectangle)){
            this.back();
            return false;
        }
        return false;
    }
}
