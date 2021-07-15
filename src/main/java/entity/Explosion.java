package entity;

import ResourceManager.ResourceManager;
import other.Audio;

import java.awt.*;

/**
 * @Author ws
 * @Date 2021/7/15 19:09
 */
// 爆炸
public class Explosion {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isAlive = true;

    public int step = 0;

    public boolean isAlive() {
        return isAlive;
    }

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = ResourceManager.explodes[0].getWidth();
        this.height = ResourceManager.explodes[0].getHeight();
        // 爆炸音乐
        new Thread(()->new Audio("audio/explode.wav").play()).start();
    }

    // 画tank
    public void fillRect(Graphics g) {
        if (!isAlive) return;
        if (step >= ResourceManager.explodes.length) {
            this.die();
        } else {
            g.drawImage(ResourceManager.explodes[step], x, y, null);
            step++;
        }

    }

    private void die() {
        this.isAlive = false;
    }

}
