package client.chainOfResponsibility;


import client.entity.AbstractGameObject;
import client.entity.Tank;
import client.entity.Wall;

/**
 * @Author ws
 * @Date 2021/7/16 18:28
 */
public class TankAndWallCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if (go1 instanceof Tank && go2 instanceof Wall) {
            Tank tank = (Tank) go1;
            Wall wall = (Wall) go2;
            return tank.collisionWithWall(wall);
        } else if (go2 instanceof Tank && go1 instanceof Wall) {
            return collide(go2, go1);
        }
        return false;
    }
}
