package frame;

import constant.Direction;
import constant.Group;
import entity.Bullet;
import entity.Tank;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * @Author ws
 * @Date 2021/7/14 19:29
 */
public class TankFrame extends Frame {

    private static final int GAME_WIDTH = 800;
    private static final int GAME_HEIGHT = 600;
    // 利用面向对象的思想,把tank的一系列操作封装在tank类里面--->封装
    private Tank tank;
    private Tank tank2;
    private ArrayList <Bullet> bullets;

    private TankFrame() throws HeadlessException {
        this.setTitle("Tank Frame");
        this.setLocation(400, 100);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.addKeyListener(new TankKeyListener());

        this.tank = new Tank(100, 100, Direction.Down, Group.Good);
        this.tank2 = new Tank(100, 200, Direction.Right, Group.Bad);
        this.bullets=new ArrayList<>();
    }

    // 单例
    private static class TankFrameHolder {
        private static final TankFrame t = new TankFrame();
    }

    public static TankFrame getInstance() {
        return TankFrameHolder.t;
    }

    @Override
    public void paint(Graphics g) {

        Color preColor = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("bullets size:  "+bullets.size(),20,50);
        g.setColor(preColor);

        tank.fillRect(g);
        tank2.fillRect(g);
        for (Bullet bullet : bullets) {
            bullet.fillRect(g);
        }
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    // 解决tank闪烁问题,利用双缓冲提前在内存把我们的图片画好,再调用显卡的画笔,显示一整个图片
    Image offScreenImage = null;

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        // 提前创建黑色背景画布
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        // 调用上面的paint()方法,画出坦克
        paint(gOffScreen);
        // 调用显卡的画笔,一次性将提前画好了的完整的tank显示在屏幕上,不然大的图片会出现,一部分还在内存,
        // 一部分已经加载到了显存,被显卡的画笔画在了屏幕上
        // 最终出现我们tank闪烁的现象
        g.drawImage(offScreenImage, 0, 0, null);
    }


    // 里面全是空方法,非适配器模式,方便我们根据需求重写自己需要的方法
    private class TankKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            tank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            tank.keyReleased(e);
        }
    }


}
