package client.strategy.fire;

import client.ResourceManager.ResourceManager;
import client.entity.Bullet;
import client.entity.PlayerTank;
import client.frame.TankFrame;

/**
 * @Author ws
 * @Date 2021/7/16 9:03
 */
public class DefaultFireStrategy implements FireStrategy{
    @Override
    public void fire(PlayerTank playerTank) {
        int bx = playerTank.getX() + ResourceManager.goodTankU.getWidth() / 2 - ResourceManager.bulletU.getWidth() / 2;
        int by = playerTank.getY() + ResourceManager.goodTankU.getHeight() / 2 - ResourceManager.bulletU.getHeight() / 2;
        Bullet bullet = new Bullet(bx, by, playerTank.getDir(), playerTank.getGroup());
        TankFrame.getInstance().getGameModel().add(bullet);
    }
}
