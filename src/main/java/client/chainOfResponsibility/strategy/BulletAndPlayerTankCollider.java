package client.chainOfResponsibility.strategy;

import client.chainOfResponsibility.Collider;
import client.entity.AbstractGameObject;
import client.entity.Bullet;
import client.entity.PlayerTank;

/**
 * @Author ws
 * @Date 2021/7/20 9:03
 */
public class BulletAndPlayerTankCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if (go1 instanceof PlayerTank && go2 instanceof Bullet) {
            PlayerTank playerTank = (PlayerTank) go1;
            Bullet bullet = (Bullet) go2;
            return bullet.collisionWithPlayerTank(playerTank);
        }else if (go2 instanceof PlayerTank && go1 instanceof Bullet){
            collide(go2,go1);
        }
        return false;
    }
}
