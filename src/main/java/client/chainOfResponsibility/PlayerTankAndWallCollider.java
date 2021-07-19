package client.chainOfResponsibility;

import client.entity.AbstractGameObject;
import client.entity.PlayerTank;
import client.entity.Wall;

/**
 * @Author ws
 * @Date 2021/7/19 12:47
 */
public class PlayerTankAndWallCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if (go1 instanceof PlayerTank && go2 instanceof Wall) {
            PlayerTank playerTank = (PlayerTank) go1;
            Wall wall = (Wall) go2;
            return playerTank.collisionWithWall(wall);
        } else if (go2 instanceof PlayerTank && go1 instanceof Wall) {
            collide(go2, go1);
        }
        return false;
    }
}
