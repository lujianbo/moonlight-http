package com.lujianbo.app.http.common;

import com.google.common.net.HostAndPort;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Created by jianbo on 2017/3/29.
 */
public class LocationMapping {

    public static HostAndPort getAddress(HttpRequest request){
        return HostAndPort.fromParts("127.0.0.1",8080);
    }
}
