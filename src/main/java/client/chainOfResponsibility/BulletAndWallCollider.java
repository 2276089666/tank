package client.chainOfResponsibility;


import client.entity.AbstractGameObject;
import client.entity.Bullet;
import client.entity.Tank;
import client.entity.Wall;

/**
 * @Author ws
 * @Date 2021/7/16 15:33
 */
public class BulletAndWallCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if (go1 instanceof Bullet && go2 instanceof Wall) {
            Bullet bullet = (Bullet) go1;
            Wall wall = (Wall) go2;
            return bullet.collisionWithWall(wall);
        } else if (go2 instanceof Bullet && go1 instanceof Tank) {
            return this.collide(go2, go1);
        }
        return false;
    }
}
