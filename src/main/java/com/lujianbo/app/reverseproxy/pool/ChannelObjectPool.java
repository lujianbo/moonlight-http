package com.lujianbo.app.reverseproxy.pool;

import com.google.common.net.HostAndPort;
import io.netty.channel.Channel;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * Created by jianbo on 2017/3/28.
 */
public class ChannelObjectPool {

    private KeyedObjectPool<HostAndPort, Channel> channelKeyedObjectPool;

    public ChannelObjectPool(GenericKeyedObjectPoolConfig config) {
        build(config);
    }

    public ChannelObjectPool() {
        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMaxTotalPerKey(64);
        config.setMaxTotal(1024 * 1204);
        config.setMaxIdlePerKey(6);//一个连接最多6个空闲
        build(config);
    }

    private void build(GenericKeyedObjectPoolConfig config){
        this.channelKeyedObjectPool = new GenericKeyedObjectPool<>(new HttpClientPooledObjectFactory(ConnectorBuilder.getInstance()), config);
    }

    public Channel getChannel(HostAndPort key) throws Exception {
        return channelKeyedObjectPool.borrowObject(key);
    }

    public void returnChannel(HostAndPort key, Channel channel) throws Exception {
        channelKeyedObjectPool.returnObject(key, channel);
    }

    public void invalidateChannel(HostAndPort key, Channel channel) throws Exception {
        channelKeyedObjectPool.invalidateObject(key, channel);
    }

}
