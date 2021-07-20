package net.handler;

import client.entity.PlayerTank;
import client.frame.TankFrame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.message.TankJoinMessage;

/**
 * @Author ws
 * @Date 2021/7/19 16:57
 */
public class ClientHandler extends SimpleChannelInboundHandler<TankJoinMessage> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        PlayerTank playerTank = TankFrame.getInstance().getGameModel().getPlayerTank();
        TankJoinMessage tankJoinMessage = new TankJoinMessage(playerTank);
        ctx.writeAndFlush(tankJoinMessage);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TankJoinMessage msg) throws Exception {
        System.out.println(msg);
        msg.handle();
    }
}
