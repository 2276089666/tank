package client.chainOfResponsibility;


import client.entity.AbstractGameObject;
import client.entity.Bullet;
import client.entity.Tank;

/**
 * @Author ws
 * @Date 2021/7/16 15:20
 */
public class BulletAndTankCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if (go1 instanceof Bullet && go2 instanceof Tank) {
            Bullet bullet = (Bullet) go1;
            Tank tank = (Tank) go2;
            return bullet.collisionWithTank(tank);
        } else if (go2 instanceof Bullet && go1 instanceof Tank) {
            return this.collide(go2, go1);
        }
        return false;
    }
}
