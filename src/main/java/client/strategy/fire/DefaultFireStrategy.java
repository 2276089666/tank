package client.strategy.fire;

import client.ResourceManager.ResourceManager;
import client.entity.Bullet;
import client.entity.PlayerTank;
import client.frame.TankFrame;
import net.client.NetClient;
import net.message.BulletNewMessage;

/**
 * @Author ws
 * @Date 2021/7/16 9:03
 */
public class DefaultFireStrategy implements FireStrategy{
    @Override
    public void fire(PlayerTank playerTank) {
        int bx = playerTank.getX() + ResourceManager.goodTankU.getWidth() / 2 - ResourceManager.goodBulletU.getWidth() / 2;
        int by = playerTank.getY() + ResourceManager.goodTankU.getHeight() / 2 - ResourceManager.goodBulletU.getHeight() / 2;
        Bullet bullet = new Bullet(bx, by, playerTank.getDir(), playerTank.getGroup(),playerTank.getId());
        TankFrame.getInstance().getGameModel().add(bullet);
        // 同步远程client的子弹位置
        NetClient.getInstance().send(new BulletNewMessage(bullet));

    }
}
