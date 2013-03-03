package com.github.cb372.imperials.test;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * Author: chris
 * Created: 3/3/13
 */
public class MockService extends Service<MockConfig> {
    public static void main(String[] args) throws Exception {
        new MockService().run(args);
    }

    @Override
    public void initialize(Bootstrap<MockConfig> bootstrap) {
    }

    @Override
    public void run(MockConfig configuration, Environment environment) throws Exception {
        environment.addResource(new MockResource());
    }
}
