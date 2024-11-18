package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ResponsesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Responses getResponsesSample1() {
        return new Responses().id(1L).responseId(1).answer("answer1");
    }

    public static Responses getResponsesSample2() {
        return new Responses().id(2L).responseId(2).answer("answer2");
    }

    public static Responses getResponsesRandomSampleGenerator() {
        return new Responses().id(longCount.incrementAndGet()).responseId(intCount.incrementAndGet()).answer(UUID.randomUUID().toString());
    }
}
