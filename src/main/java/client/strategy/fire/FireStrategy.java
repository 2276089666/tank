package client.strategy.fire;

import client.entity.PlayerTank;

import java.io.Serializable;

/**
 * @Author ws
 * @Date 2021/7/16 9:02
 */
public interface FireStrategy extends Serializable {
    void fire(PlayerTank playerTank);
}
