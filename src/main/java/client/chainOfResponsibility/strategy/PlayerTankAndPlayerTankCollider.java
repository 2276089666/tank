package client.chainOfResponsibility.strategy;

import client.chainOfResponsibility.Collider;
import client.entity.AbstractGameObject;
import client.entity.PlayerTank;

/**
 * @Author ws
 * @Date 2021/7/20 8:46
 */
public class PlayerTankAndPlayerTankCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if (go1 instanceof PlayerTank && go2 instanceof PlayerTank&&go1!=go2) {
            PlayerTank playerTank = (PlayerTank) go1;
            PlayerTank playerTank1 = (PlayerTank) go2;
            return playerTank.collisionWithPlayerTank(playerTank1);
        }
        return false;
    }
}
