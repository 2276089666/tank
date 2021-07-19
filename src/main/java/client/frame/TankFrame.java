package client.frame;


import client.model.GameModel;
import client.util.PropertiesUtil;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

/**
 * @Author ws
 * @Date 2021/7/14 19:29
 */
// MVC的View层
public class TankFrame extends Frame {

    public static final int GAME_WIDTH = Integer.parseInt(PropertiesUtil.get("GAME_WIDTH"));
    public static final int GAME_HEIGHT = Integer.parseInt(PropertiesUtil.get("GAME_HEIGHT"));
    private Image offScreenImage = null;

    private GameModel gameModel = null;

    private TankFrame() throws HeadlessException {
        this.setTitle("Tank Frame");
        this.setLocation(400, 100);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.addKeyListener(new TankKeyListener());
        this.gameModel = GameModel.getInstance();
    }

    public static TankFrame getInstance() {
        return TankFrameHolder.tankFrame;
    }

    @Override
    public void paint(Graphics g) {
        this.gameModel.paint(g);
    }

    // 解决tank闪烁问题,利用双缓冲提前在内存把我们的图片画好,再调用显卡的画笔,显示一整个图片
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

    public GameModel getGameModel() {
        return this.gameModel;
    }

    private void load() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File file = new File("save.txt");
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            this.gameModel = (GameModel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 要求gameModel里面的所有有关对象都实现Serializable接口
    private void save() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File("save.txt");
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(gameModel);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 单例
    private static class TankFrameHolder {
        private static final TankFrame tankFrame = new TankFrame();
    }

    // 里面全是空方法,非适配器模式,方便我们根据需求重写自己需要的方法
    private class TankKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_S) {
                // 存盘
                save();
                System.out.println("存档完成");
            } else if (key == KeyEvent.VK_L) {
                // 加载存档
                load();
                System.out.println("成功加载存档");
            } else {
                gameModel.getPlayerTank().keyPressed(e);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            gameModel.getPlayerTank().keyReleased(e);
        }
    }

}
