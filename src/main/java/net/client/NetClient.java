package net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.coder.TankJoinMessageDecoder;
import net.coder.TankJoinMessageEncoder;
import net.handler.ClientHandler;
import net.message.TankJoinMessage;

import java.net.InetSocketAddress;

/**
 * @Author ws
 * @Date 2021/7/19 16:48
 */
public class NetClient {

    Channel client = null;

    // 单例
    private NetClient() {
    }

    public void send(TankJoinMessage tankJoinMessage) {
        client.writeAndFlush(tankJoinMessage);
    }

    private static class NetClientHolder {
        private static final NetClient netClient = new NetClient();
    }

    public static NetClient getInstance() {
        return NetClientHolder.netClient;
    }

    public void connect() {
        NioEventLoopGroup group = null;
        try {
            group = new NioEventLoopGroup(2);
            ChannelFuture connect = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new TankJoinMessageEncoder());
                            pipeline.addLast(new TankJoinMessageDecoder());
                            pipeline.addLast(new ClientHandler());
                        }
                    })
                    .connect(new InetSocketAddress("localhost", 9090));
            connect.sync();
            client = connect.channel();
            connect.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (group != null) group.shutdownGracefully();
        }
    }
}
