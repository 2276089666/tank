package client.entity;

import java.awt.*;

/**
 * @Author ws
 * @Date 2021/7/16 11:12
 */
public class Wall extends AbstractGameObject {
    private int x;
    private int y;
    private int width;
    private int height;
    private Rectangle rectangle = null;

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(x, y, width, height);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public void fillRect(Graphics g) {
        Color preColor = g.getColor();
        g.setColor(Color.CYAN);
        g.fillRect(x, y, width, height);
        g.setColor(preColor);
    }

    @Override
    public boolean isAlive() {
        return true;
    }
}
