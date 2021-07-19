package client.strategy.fire;

import client.ResourceManager.ResourceManager;
import client.constant.Direction;
import client.entity.Bullet;
import client.entity.PlayerTank;
import client.frame.TankFrame;

/**
 * @Author ws
 * @Date 2021/7/16 9:24
 */
public class FourDirFireStrategy implements FireStrategy{
    @Override
    public void fire(PlayerTank playerTank) {
        int bx = playerTank.getX() + ResourceManager.goodTankU.getWidth() / 2 - ResourceManager.bulletU.getWidth() / 2;
        int by = playerTank.getY() + ResourceManager.goodTankU.getHeight() / 2 - ResourceManager.bulletU.getHeight() / 2;
        for (Direction dir : Direction.values()) {
            TankFrame.getInstance().getGameModel().add(new Bullet(bx, by, dir, playerTank.getGroup()));
        }
    }
}
