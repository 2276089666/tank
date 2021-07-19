package client.entity;

import client.ResourceManager.ResourceManager;
import client.other.Audio;

import java.awt.*;

/**
 * @Author ws
 * @Date 2021/7/15 19:09
 */
// 爆炸
public class Explosion extends AbstractGameObject {
    public int step = 0;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isAlive = true;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = ResourceManager.explodes[0].getWidth();
        this.height = ResourceManager.explodes[0].getHeight();
        // 爆炸音乐
        new Thread(() -> new Audio("audio/explode.wav").play()).start();
    }

    public boolean isAlive() {
        return isAlive;
    }

    // 画爆炸
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
