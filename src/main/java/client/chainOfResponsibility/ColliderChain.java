package client.chainOfResponsibility;


import client.entity.AbstractGameObject;
import client.util.PropertiesUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ws
 * @Date 2021/7/16 18:52
 */
// 责任链,其实也是一个碰撞器
public class ColliderChain implements Collider {
    private List<Collider> colliderList = null;

    public ColliderChain() {
        initColliderList();
    }

    private void initColliderList() {
        colliderList = new ArrayList<>();
        String colliderNames = PropertiesUtil.get("Collider");
        String[] colliderName = colliderNames.split(",");
        for (String s : colliderName) {
            try {
                Class<?> aClass = Class.forName("client.chainOfResponsibility." + s);
                Collider collider = (Collider) aClass.getDeclaredConstructor().newInstance();
                colliderList.add(collider);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        for (Collider collider : colliderList) {
            // 如果发生碰撞即collide()的返回为true,退出遍历
            // 结束链条
            if (collider.collide(go1, go2)) {
                return true;
            }
        }
        return false;
    }
}
