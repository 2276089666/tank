package net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.coder.Decoder;
import net.coder.Encoder;
import net.handler.ClientHandler;
import net.message.AbstractMessage;

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

    public void send(AbstractMessage abstractMessage) {
      if (client==null) return;
      client.writeAndFlush(abstractMessage);

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
                            pipeline.addLast(new Encoder());
                            pipeline.addLast(new Decoder());
                            pipeline.addLast(new ClientHandler());
                        }
                    })
                    .connect(new InetSocketAddress("192.168.31.153", 9090));


            connect.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        System.out.println("not connected!");
                    } else {
                        System.out.println("connected!");
                        // initialize the channel
                        client = future.channel();
                    }
                }
            });

            connect.channel().closeFuture().sync();
            System.out.println("connection closed!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (group != null) group.shutdownGracefully();
        }
    }
}
