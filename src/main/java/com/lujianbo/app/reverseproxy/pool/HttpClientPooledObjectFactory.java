package com.lujianbo.app.reverseproxy.pool;

import com.google.common.net.HostAndPort;
import io.netty.channel.Channel;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.function.Function;

/**
 * Created by jianbo on 2017/3/28.
 */
public class HttpClientPooledObjectFactory implements KeyedPooledObjectFactory<HostAndPort, Channel> {

    private ConnectorBuilder builder;

    public HttpClientPooledObjectFactory(ConnectorBuilder builder){
        this.builder=builder;
    }

    /**
     * is called whenever a new instance is needed.
     */
    @Override
    public PooledObject<Channel> makeObject(HostAndPort key) throws Exception {
        return new DefaultPooledObject<>(builder.build(key));
    }

    /**
     * is invoked on every instance when it is being "dropped" from the pool
     */
    @Override
    public void destroyObject(HostAndPort key, PooledObject<Channel> p) throws Exception {
        if (p.getObject().isActive()) {
            p.getObject().close();
        }
    }

    /**
     * may be invoked on activated instances to make sure they can be borrowed from the pool. validateObject may also be used to test an instance being returned to the pool before it is passivated. It will only be invoked on an activated instance.
     */
    @Override
    public boolean validateObject(HostAndPort key, PooledObject<Channel> p) {
        Channel channel = p.getObject();
        return channel.isActive();
    }

    /**
     * is invoked on every instance that has been passivated before it is borrowed from the pool.
     */
    @Override
    public void activateObject(HostAndPort key, PooledObject<Channel> p) throws Exception {
        //do nothing
    }

    /**
     * is invoked on every instance when it is returned to the pool.
     */
    @Override
    public void passivateObject(HostAndPort key, PooledObject<Channel> p) throws Exception {
        //clear buff
        p.getObject().flush();
    }
}
