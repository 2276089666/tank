package client.chainOfResponsibility;

import client.entity.AbstractGameObject;

import java.io.Serializable;

/**
 * @Author ws
 * @Date 2021/7/16 15:18
 */
// 碰撞器
public interface Collider extends Serializable {
    // 返回true说明碰撞了,后面的遍历要终止
    boolean collide(AbstractGameObject go1, AbstractGameObject go2);
}
