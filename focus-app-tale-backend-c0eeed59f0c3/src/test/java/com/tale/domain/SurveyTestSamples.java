package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SurveyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Survey getSurveySample1() {
        return new Survey().id(1L).surveyId(1).title("title1").description("description1").createdByUserId(1).status(1);
    }

    public static Survey getSurveySample2() {
        return new Survey().id(2L).surveyId(2).title("title2").description("description2").createdByUserId(2).status(2);
    }

    public static Survey getSurveyRandomSampleGenerator() {
        return new Survey()
            .id(longCount.incrementAndGet())
            .surveyId(intCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdByUserId(intCount.incrementAndGet())
            .status(intCount.incrementAndGet());
    }
}
