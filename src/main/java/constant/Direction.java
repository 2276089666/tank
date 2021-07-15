package constant;

import java.util.Random;

/**
 * @Author ws
 * @Date 2021/7/14 20:35
 */
public enum Direction {
    Left,Right,Up,Down;
    private static Random random=new Random();
    public static Direction getRandomDirection(){
        return Direction.values()[random.nextInt(Direction.values().length)];
    }
}
