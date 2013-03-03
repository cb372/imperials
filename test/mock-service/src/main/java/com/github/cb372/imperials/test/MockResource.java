package com.github.cb372.imperials.test;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.annotation.Timed;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Author: chris
 * Created: 3/3/13
 */
@Path("/hello")
@Produces("text/plain")
public class MockResource {

    @GET
    @Timed
    public String sayHello() {
        return "hello";
    }
}
