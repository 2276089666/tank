package client.entity;

import java.awt.*;
import java.io.Serializable;

/**
 * @Author ws
 * @Date 2021/7/16 11:09
 */
public abstract class AbstractGameObject implements Serializable {
    // 在屏幕上画出对应的tank,子弹,爆炸啥的
    public abstract void fillRect(Graphics g);

    public abstract boolean isAlive();
}
