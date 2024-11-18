package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PointSpendOptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PointSpendOption getPointSpendOptionSample1() {
        return new PointSpendOption().id(1L).redemptionOptionId(1).description("description1").pointsRequired(1).availableQuantity(1);
    }

    public static PointSpendOption getPointSpendOptionSample2() {
        return new PointSpendOption().id(2L).redemptionOptionId(2).description("description2").pointsRequired(2).availableQuantity(2);
    }

    public static PointSpendOption getPointSpendOptionRandomSampleGenerator() {
        return new PointSpendOption()
            .id(longCount.incrementAndGet())
            .redemptionOptionId(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .pointsRequired(intCount.incrementAndGet())
            .availableQuantity(intCount.incrementAndGet());
    }
}
