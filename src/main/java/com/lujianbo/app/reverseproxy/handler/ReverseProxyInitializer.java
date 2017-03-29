package com.lujianbo.app.reverseproxy.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by jianbo on 2017/3/28.
 */
public class ReverseProxyInitializer extends ChannelInitializer<SocketChannel>{

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("HttpRequestDecoder",new HttpRequestDecoder());
        ch.pipeline().addLast("HttpResponseEncoder",new HttpResponseEncoder());
        ch.pipeline().addLast("ReverseProxyHandler",new ReverseProxyHandler());
    }
}
