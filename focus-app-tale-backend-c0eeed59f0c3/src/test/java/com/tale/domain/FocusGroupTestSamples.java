package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FocusGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FocusGroup getFocusGroupSample1() {
        return new FocusGroup().id(1L).focusGroupId(1).name("name1").description("description1").userCategory("userCategory1").status(1);
    }

    public static FocusGroup getFocusGroupSample2() {
        return new FocusGroup().id(2L).focusGroupId(2).name("name2").description("description2").userCategory("userCategory2").status(2);
    }

    public static FocusGroup getFocusGroupRandomSampleGenerator() {
        return new FocusGroup()
            .id(longCount.incrementAndGet())
            .focusGroupId(intCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .userCategory(UUID.randomUUID().toString())
            .status(intCount.incrementAndGet());
    }
}
