package com.lujianbo.app.reverseproxy.pool;

import com.google.common.net.HostAndPort;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * Created by jianbo on 2017/3/28.
 */
public class ConnectorBuilder {

    private static EventLoopGroup executors = new NioEventLoopGroup();

    private ConnectorBuilder() {
        //lots of initialization code
    }

    public static ConnectorBuilder getInstance() {
        return ConnectorBuilderHolder.instance;
    }

    public Channel build(HostAndPort key){
        try {
            return connect(key.getHost(), key.getPort(), () -> new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("HttpRequestEncoder", new HttpRequestEncoder());
                    ch.pipeline().addLast("HttpResponseDecoder", new HttpResponseDecoder());
                }
            });
        }catch (Exception e){
            return null;
        }
    }

    private Channel connect(String host, int port, Supplier<ChannelHandler> initSupplier) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(executors)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(initSupplier.get());
        return b.connect(host, port).sync().channel();
    }

    private static class ConnectorBuilderHolder {
        public static final ConnectorBuilder instance = new ConnectorBuilder();
    }
}
