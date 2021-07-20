package client.model;

import client.chainOfResponsibility.ColliderChain;
import client.constant.Direction;
import client.constant.Group;
import client.entity.*;
import client.util.PropertiesUtil;
import io.netty.util.CharsetUtil;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

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
    private Random random = new Random();

    private GameModel() {
        initGameObject();
    }

    public static GameModel getInstance() {
        return GameModelHolder.gameModel;
    }

    public void initGameObject() {
        // 随机生成玩家tank位置
        this.playerTank = new PlayerTank(900 - random.nextInt(800), 900 - random.nextInt(100), Direction.values()[random.nextInt(Direction.values().length)], Group.Good);
        this.objects = new ArrayList<>();
        this.objects.add(playerTank);
        for (int i = 0; i < Integer.parseInt(PropertiesUtil.get("initTankCount")); i++) {
            this.objects.add(new Tank(50 + 150 * i, 100 + 30 * i, Direction.Down, Group.Bad));
        }
        for (int i = 0; i < Integer.parseInt(PropertiesUtil.get("initWallCount")); i++) {
            this.objects.add(new Wall(100 + 300 * i, 300 + 100 * i, (int) (100 + 100 * Math.random()), (int) (200 + 100 * Math.random())));
        }
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
        int playerSize = 0;
        for (AbstractGameObject object : objects) {
            if (object instanceof Bullet) {
                bulletsSize++;
            } else if (object instanceof Tank) {
                enemySize++;
            } else if (object instanceof Wall) {
                wallSize++;
            } else if (object instanceof PlayerTank) {
                playerSize++;
            }
        }
        g.drawString("bullets size:  " + bulletsSize, 20, 50);
        g.drawString("enemy size:  " + enemySize, 20, 70);
        g.drawString("wall size:  " + wallSize, 20, 90);
        g.drawString("player size:  " + playerSize, 20, 110);
        g.drawString(new String("                 \u2191".getBytes(), CharsetUtil.UTF_8), 20, 130);
        g.drawString(new String("move: \u2190  \u2193  \u2192".getBytes(), CharsetUtil.UTF_8), 20, 150);
        g.drawString("fire:   space", 20, 180);
        g.drawString("game save : s", 20, 200);
        g.drawString("game load : l", 20, 220);
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


    public void updateOrInsertByUUID(PlayerTank playerTank) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof PlayerTank) {
                PlayerTank playerTank1 = (PlayerTank) objects.get(i);
                if (playerTank1.getId().equals(playerTank.getId())) {
                    objects.remove(i);
                    objects.add(playerTank);
                    return;
                }
            }
        }
        objects.add(playerTank);
    }

    public static class GameModelHolder {
        private static final GameModel gameModel = new GameModel();
    }

}
