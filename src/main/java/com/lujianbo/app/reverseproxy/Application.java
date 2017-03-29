package com.lujianbo.app.reverseproxy;

import com.lujianbo.app.reverseproxy.handler.ReverseProxyInitializer;

/**
 * Created by jianbo on 2017/3/28.
 */
public class Application {

    public static void main(String[] args) {

        ProxyServer proxyServer = new ProxyServer(6543,new ReverseProxyInitializer());
        proxyServer.start();

    }
}
