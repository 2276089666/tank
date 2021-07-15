import frame.TankFrame;

/**
 * @Author ws
 * @Date 2021/7/14 19:32
 */
public class Main {
    public static void main(String[] args) {
        TankFrame tankFrame = TankFrame.getInstance();
        tankFrame.setVisible(true);
        while (true){
            try {
                // 重画tank
                tankFrame.repaint();
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
