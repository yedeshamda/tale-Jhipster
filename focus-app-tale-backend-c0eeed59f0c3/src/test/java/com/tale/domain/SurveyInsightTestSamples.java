package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SurveyInsightTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SurveyInsight getSurveyInsightSample1() {
        return new SurveyInsight().id(1L).insightId(1).insights("insights1").surveyId(1).createdByUserId(1);
    }

    public static SurveyInsight getSurveyInsightSample2() {
        return new SurveyInsight().id(2L).insightId(2).insights("insights2").surveyId(2).createdByUserId(2);
    }

    public static SurveyInsight getSurveyInsightRandomSampleGenerator() {
        return new SurveyInsight()
            .id(longCount.incrementAndGet())
            .insightId(intCount.incrementAndGet())
            .insights(UUID.randomUUID().toString())
            .surveyId(intCount.incrementAndGet())
            .createdByUserId(intCount.incrementAndGet());
    }
}
