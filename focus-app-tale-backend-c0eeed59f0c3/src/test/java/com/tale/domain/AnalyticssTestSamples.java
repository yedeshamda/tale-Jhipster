package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AnalyticssTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Analyticss getAnalyticssSample1() {
        return new Analyticss().id(1L).analyticsId(1).insights("insights1");
    }

    public static Analyticss getAnalyticssSample2() {
        return new Analyticss().id(2L).analyticsId(2).insights("insights2");
    }

    public static Analyticss getAnalyticssRandomSampleGenerator() {
        return new Analyticss()
            .id(longCount.incrementAndGet())
            .analyticsId(intCount.incrementAndGet())
            .insights(UUID.randomUUID().toString());
    }
}
