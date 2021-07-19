package client.chainOfResponsibility;

import client.entity.AbstractGameObject;
import client.entity.PlayerTank;
import client.entity.Tank;

/**
 * @Author ws
 * @Date 2021/7/19 11:46
 */
public class PlayerTankAndTankCollider implements Collider{
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if (go1 instanceof PlayerTank && go2 instanceof Tank ) {
            PlayerTank playerTank = (PlayerTank) go1;
            Tank tank2 = (Tank) go2;
            return playerTank.collisionWithTank(tank2);
        }else if (go1 instanceof Tank && go2 instanceof PlayerTank ){
            collide(go2,go1);
        }
        return false;
    }
}
