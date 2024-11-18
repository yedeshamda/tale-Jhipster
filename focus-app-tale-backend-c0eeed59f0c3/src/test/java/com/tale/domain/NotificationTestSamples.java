package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Notification getNotificationSample1() {
        return new Notification().id(1L).notificationId(1).message("message1");
    }

    public static Notification getNotificationSample2() {
        return new Notification().id(2L).notificationId(2).message("message2");
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(longCount.incrementAndGet())
            .notificationId(intCount.incrementAndGet())
            .message(UUID.randomUUID().toString());
    }
}
