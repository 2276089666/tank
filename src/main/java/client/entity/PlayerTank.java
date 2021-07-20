package client.entity;

import client.ResourceManager.ResourceManager;
import client.constant.Direction;
import client.constant.Group;
import client.frame.TankFrame;
import client.model.GameModel;
import client.strategy.fire.FireStrategy;
import client.util.PropertiesUtil;
import net.client.NetClient;
import net.message.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * @Author ws
 * @Date 2021/7/15 15:30
 */
// 玩家
public class PlayerTank extends AbstractGameObject {
    private static final int speed = 10;
    private boolean isAlive = true;
    private int x;
    private int y;
    private Direction dir;
    private boolean left, right, up, down;
    private boolean moving = false;
    private Group group;
    private Rectangle rectangle = null;
    private int preX;
    private int preY;
    private UUID id;
    private boolean fire;

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
        this.rectangle.width = ResourceManager.goodTankU.getWidth();
        this.rectangle.height = ResourceManager.goodTankU.getHeight();
        this.id = UUID.randomUUID();
        this.fire = false;
        this.isAlive = true;
    }

    public PlayerTank(TankJoinMessage tankJoinMessage) {
        this.id = tankJoinMessage.getUuid();
        this.x = tankJoinMessage.getX();
        this.y = tankJoinMessage.getY();
        this.dir = tankJoinMessage.getDirection();
        this.group = tankJoinMessage.getGroup();
        this.rectangle = new Rectangle();
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = ResourceManager.goodTankU.getWidth();
        this.rectangle.height = ResourceManager.goodTankU.getHeight();
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

    public UUID getId() {
        return id;
    }

    public boolean isFire() {
        return fire;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    // 画tank
    public void fillRect(Graphics g) {
        if (!this.isAlive()) return;

        Color preColor = g.getColor();
        g.setColor(Color.YELLOW);
        g.drawString(id.toString(), x - 80, y - 10);
        g.setColor(preColor);

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
        boolean preIsMoving = this.moving;
        Direction preDirection = this.dir;
        if (!left && !right && !up && !down) {
            this.moving = false;
            // 同步其他client,当前tank移动停止
            NetClient.getInstance().send(new TankStopMovingMessage(this.id, this.x, this.y));
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

            // 发送tank的移动同步消息
            // 之前一直处于移动状态,不发送消息,移动键松开时再发送,减少发送的message
            if (!preIsMoving)
                NetClient.getInstance().send(new TankStartMovingMessage(this.id, this.x, this.y, this.getDir()));

            // 当前tank方向改变,同步其他client
            if (!dir.equals(preDirection))
                NetClient.getInstance().send(new TankChangeDirMessage(this.id, this.x, this.y, this.dir));

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
                this.fire = true;
                fire();
                break;
        }
        setDirection();
    }

    private void fire() {
        if (!this.isAlive()) return;
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
        GameModel.getInstance().add(new Explosion(x, y));
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
        if (rectangle.intersects(wallRectangle)) {
            this.back();
            return false;
        }
        return false;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean collisionWithPlayerTank(PlayerTank playerTank1) {
        if (!this.isAlive()) return true;
        if (!playerTank1.isAlive()) return true;
        Rectangle rectangle = this.getRectangle();
        Rectangle rectangle1 = playerTank1.getRectangle();
        if (rectangle.intersects(rectangle1)) {
            this.back();
            return false;
        }
        return false;
    }

}
