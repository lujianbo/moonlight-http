package com.lujianbo.app.http.handler;

import com.google.common.net.HostAndPort;
import com.lujianbo.app.http.common.LocationMapping;
import com.lujianbo.app.http.pool.ChannelObjectPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;

/**
 * Created by jianbo on 2017/3/28.
 */
public class ReverseProxyHandler extends ChannelInboundHandlerAdapter{

    private Channel outboundChannel;

    private static ChannelObjectPool channelObjectPool=new ChannelObjectPool();

    private HttpRelayClientHandler httpRelayClientHandler;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        httpRelayClientHandler=new HttpRelayClientHandler(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest request=(HttpRequest) msg;
            HostAndPort hostAndPort = LocationMapping.getAddress(request);
            this.outboundChannel=channelObjectPool.getChannel(hostAndPort);
            httpRelayClientHandler.setKey(hostAndPort);
            this.outboundChannel.pipeline().addLast(httpRelayClientHandler);
            //write request
            outboundChannel.writeAndFlush(msg);
        }else {
            outboundChannel.writeAndFlush(msg);
            if (msg instanceof LastHttpContent){
                resetOutboundChannel();
            }
        }
    }

    private void resetOutboundChannel(){
        this.outboundChannel=null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    @ChannelHandler.Sharable
     class HttpRelayClientHandler extends ChannelInboundHandlerAdapter {

        private Channel relayChannel;

        private HostAndPort key;

        public HttpRelayClientHandler(Channel relayChannel) {
            this.relayChannel = relayChannel;
        }

        public HostAndPort getKey() {
            return key;
        }

        public void setKey(HostAndPort key) {
            this.key = key;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            relayChannel.writeAndFlush(msg);
            if (msg instanceof LastHttpContent) {
                ctx.pipeline().remove(HttpRelayClientHandler.class);
                channelObjectPool.returnChannel(key,ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if (!ctx.channel().isActive()){
                channelObjectPool.invalidateChannel(this.key, ctx.channel());
            }
        }
    }
}
