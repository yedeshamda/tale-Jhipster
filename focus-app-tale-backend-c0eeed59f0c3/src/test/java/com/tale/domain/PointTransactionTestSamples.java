package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PointTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PointTransaction getPointTransactionSample1() {
        return new PointTransaction().id(1L).transactionId(1).description("description1").points(1).userId(1);
    }

    public static PointTransaction getPointTransactionSample2() {
        return new PointTransaction().id(2L).transactionId(2).description("description2").points(2).userId(2);
    }

    public static PointTransaction getPointTransactionRandomSampleGenerator() {
        return new PointTransaction()
            .id(longCount.incrementAndGet())
            .transactionId(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .points(intCount.incrementAndGet())
            .userId(intCount.incrementAndGet());
    }
}
