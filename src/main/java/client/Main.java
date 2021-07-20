package client;

import client.frame.TankFrame;
import client.other.Audio;
import net.client.NetClient;
import sun.nio.ch.Net;

/**
 * @Author ws
 * @Date 2021/7/14 19:32
 */
public class Main {
    public static void main(String[] args) {
        TankFrame tankFrame = TankFrame.getInstance();
        tankFrame.setLocationRelativeTo(null);
        tankFrame.setVisible(true);
        // 主背景音乐
        new Thread(() -> new Audio("audio/war1.wav").play()).start();

        new Thread(() -> {
            while (true) {
                try {
                    // 重画tank
                    tankFrame.repaint();
                    Thread.sleep(30);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        NetClient.getInstance().connect();

    }
}
