package client.model;

import client.chainOfResponsibility.ColliderChain;
import client.constant.Direction;
import client.constant.Group;
import client.entity.*;
import client.util.PropertiesUtil;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Author ws
 * @Date 2021/7/16 20:38
 */
// MVC的Model层
public class GameModel implements Serializable {

    // 利用面向对象的思想,把tank的一系列操作封装在tank类里面--->封装
    private PlayerTank playerTank = null;
    private ArrayList<AbstractGameObject> objects = null;
    private ColliderChain colliderChain = null;

    private GameModel() {
        initGameObject();
    }

    public static GameModel getInstance() {
        return GameModelHolder.gameModel;
    }

    public void initGameObject() {
        this.playerTank = new PlayerTank(100, 400, Direction.Down, Group.Good);
        this.objects = new ArrayList<>();
        this.objects.add(playerTank);
        for (int i = 0; i < Integer.parseInt(PropertiesUtil.get("initTankCount")); i++) {
            this.objects.add(new Tank(50 + 70 * i, 100, Direction.Down, Group.Bad));
        }
        this.objects.add(new Wall(300, 200, 50, 150));
        this.colliderChain = new ColliderChain();
    }

    public void add(AbstractGameObject object) {
        objects.add(object);
    }

    public void paint(Graphics g) {
        Color preColor = g.getColor();
        g.setColor(Color.WHITE);
        int bulletsSize = 0;
        int enemySize = 0;
        int wallSize = 0;
        int playerSize=0;
        for (AbstractGameObject object : objects) {
            if (object instanceof Bullet) {
                bulletsSize++;
            } else if (object instanceof Tank) {
                enemySize++;
            } else if (object instanceof Wall) {
                wallSize++;
            }else if (object instanceof PlayerTank){
                playerSize++;
            }
        }
        g.drawString("bullets size:  " + bulletsSize, 20, 50);
        g.drawString("enemy size:  " + enemySize, 20, 70);
        g.drawString("wall size:  " + wallSize, 20, 90);
        g.drawString("player size:  " + playerSize, 20, 110);
        g.setColor(preColor);


//        playerTank.fillRect(g);


        for (int i = 0; i < objects.size(); i++) {
            AbstractGameObject go1 = objects.get(i);
            // 对象死亡,从对象集合中删除,并跳过画死亡对象
            if (!go1.isAlive()) {
                objects.remove(i);
                continue;
            }
            for (int j = 0; j < objects.size(); j++) {
                AbstractGameObject go2 = objects.get(j);
                colliderChain.collide(go1, go2);
            }
            objects.get(i).fillRect(g);
        }
    }

    public PlayerTank getPlayerTank() {
        return playerTank;
    }

    public static class GameModelHolder {
        private static final GameModel gameModel = new GameModel();
    }

}
