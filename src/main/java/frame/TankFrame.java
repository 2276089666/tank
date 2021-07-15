package frame;

import constant.Direction;
import constant.Group;
import entity.Bullet;
import entity.Explosion;
import entity.PlayerTank;
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

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
    // 解决tank闪烁问题,利用双缓冲提前在内存把我们的图片画好,再调用显卡的画笔,显示一整个图片
    private Image offScreenImage = null;
    // 利用面向对象的思想,把tank的一系列操作封装在tank类里面--->封装
    private PlayerTank playerTank;
    private ArrayList<Tank> enemy;
    private ArrayList <Bullet> bullets;
    public ArrayList<Explosion> explosions;

    private TankFrame() throws HeadlessException {
        this.setTitle("Tank Frame");
        this.setLocation(400, 100);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.addKeyListener(new TankKeyListener());
        initGameObject();
    }

    public void initGameObject(){
        this.playerTank = new PlayerTank(100, 100, Direction.Down, Group.Good);
        this.bullets=new ArrayList<>();
        this.enemy=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            enemy.add(new Tank(50+50*i,200,Direction.Down,Group.Bad));
        }
        this.explosions=new ArrayList<>();
    }

    public static TankFrame getInstance() {
        return TankFrameHolder.t;
    }

    @Override
    public void paint(Graphics g) {

        Color preColor = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("bullets size:  "+bullets.size(),20,50);
        g.drawString("enemy size:  "+enemy.size(),20,70);
        g.setColor(preColor);

        playerTank.fillRect(g);
        for (int i = 0; i < enemy.size(); i++) {
            if (!enemy.get(i).isAlive){
                enemy.remove(i);
            }else {
                enemy.get(i).fillRect(g);
            }
        }

        for (int i = 0; i < bullets.size(); i++) {

            for (int j = 0; j < enemy.size(); j++) {
                bullets.get(i).collisionWithTank(enemy.get(j));
            }

            if (!bullets.get(i).isAlive()){
                bullets.remove(i);
            }else {
                bullets.get(i).fillRect(g);
            }
        }

        for (int i = 0; i < explosions.size(); i++) {
            if (!explosions.get(i).isAlive()){
                explosions.remove(i);
            }else {
                explosions.get(i).fillRect(g);
            }
        }
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

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

    // 单例
    private static class TankFrameHolder {
        private static final TankFrame t = new TankFrame();
    }

    // 里面全是空方法,非适配器模式,方便我们根据需求重写自己需要的方法
    private class TankKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            playerTank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            playerTank.keyReleased(e);
        }
    }


}
