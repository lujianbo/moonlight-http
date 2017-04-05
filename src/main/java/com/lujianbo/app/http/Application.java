package com.lujianbo.app.http;

import com.lujianbo.app.http.handler.ReverseProxyInitializer;

/**
 * Created by jianbo on 2017/3/28.
 */
public class Application {

    public static void main(String[] args) {

        HTTPServer HTTPServer = new HTTPServer(6543,new ReverseProxyInitializer());
        HTTPServer.start();

    }
}
