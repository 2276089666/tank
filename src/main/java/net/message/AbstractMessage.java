package net.message;



/**
 * @Author ws
 * @Date 2021/7/20 11:07
 */
public abstract class AbstractMessage {

    public abstract byte[] objectToByteArr();

    public abstract void parseByteArrToObject(byte[] data);

    public abstract void handle();

    public abstract int getType();

}
