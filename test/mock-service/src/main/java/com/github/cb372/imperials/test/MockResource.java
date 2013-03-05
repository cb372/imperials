package com.github.cb372.imperials.test;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.annotation.Timed;
import com.yammer.metrics.core.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Author: chris
 * Created: 3/3/13
 */
@Path("/hello")
@Produces("text/plain")
public class MockResource {

    Random rnd = new Random();
    Gauge unscopedGauge = Metrics.newGauge(MockResource.class, "unscoped.gauge", new Gauge<Double>() {
        @Override
        public Double value() {
            return 0.75;
        }
    });

    Gauge scopedGauge = Metrics.newGauge(MockResource.class, "scoped.gauge", "custom.scope", new Gauge<Double>() {
        @Override
        public Double value() {
            return 1.25;
        }
    });

    Counter unscopedCounter = Metrics.newCounter(MockResource.class, "unscoped.counter");
    Counter scopedCounter = Metrics.newCounter(MockResource.class, "scoped.counter", "custom.scope");
    Meter unscopedMeter = Metrics.newMeter(MockResource.class, "unscoped.meter", "my.event", TimeUnit.MILLISECONDS);
    Meter scopedMeter = Metrics.newMeter(MockResource.class, "scoped.meter", "custom.scope", "my.event", TimeUnit.MILLISECONDS);
    Histogram unscopedHistogram = Metrics.newHistogram(MockResource.class, "unscoped.histogram");
    Histogram scopedHistogram = Metrics.newHistogram(MockResource.class, "scoped.histogram", "custom.scope");
    Timer unscopedTimer = Metrics.newTimer(MockResource.class, "unscoped.timer");
    Timer scopedTimer = Metrics.newTimer(MockResource.class, "scoped.timer", "custom.scope");

    @GET
    @Timed
    public String sayHello() {
        unscopedCounter.inc();
        if (rnd.nextBoolean()) {
            scopedCounter.inc();
        }

        unscopedMeter.mark();
        if (rnd.nextBoolean()) {
            scopedMeter.mark();
        }

        unscopedHistogram.update(rnd.nextInt(100));
        scopedHistogram.update(rnd.nextInt(100));

        unscopedTimer.update(rnd.nextInt(100), TimeUnit.MILLISECONDS);
        scopedTimer.update(rnd.nextInt(100), TimeUnit.MILLISECONDS);

        return "hello";
    }
}
