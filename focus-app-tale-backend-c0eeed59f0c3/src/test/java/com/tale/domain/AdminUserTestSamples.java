package com.tale.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AdminUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AdminUser getAdminUserSample1() {
        return new AdminUser().id(1L).adminId(1);
    }

    public static AdminUser getAdminUserSample2() {
        return new AdminUser().id(2L).adminId(2);
    }

    public static AdminUser getAdminUserRandomSampleGenerator() {
        return new AdminUser().id(longCount.incrementAndGet()).adminId(intCount.incrementAndGet());
    }
}
