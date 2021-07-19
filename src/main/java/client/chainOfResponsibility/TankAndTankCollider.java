package client.chainOfResponsibility;


import client.entity.AbstractGameObject;
import client.entity.Tank;

/**
 * @Author ws
 * @Date 2021/7/16 19:59
 */
public class TankAndTankCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if (go1 instanceof Tank && go2 instanceof Tank && go1 != go2) {
            Tank tank = (Tank) go1;
            Tank tank2 = (Tank) go2;
            return tank.collisionWithTank(tank2);
        }
        return false;
    }
}
