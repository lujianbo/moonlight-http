package com.lujianbo.app.http.common.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * Created by jianbo on 2017/3/31.
 */
public class HelloWorldCommand extends HystrixCommand<String>{


    protected HelloWorldCommand(HystrixCommandGroupKey group) {
        super(group);
    }

    protected HelloWorldCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool) {
        super(group, threadPool);
    }

    protected HelloWorldCommand(HystrixCommandGroupKey group, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, executionIsolationThreadTimeoutInMilliseconds);
    }

    protected HelloWorldCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, threadPool, executionIsolationThreadTimeoutInMilliseconds);
    }

    protected HelloWorldCommand(Setter setter) {
        super(setter);
    }

    @Override
    protected String run() throws Exception {
        return null;
    }
}
