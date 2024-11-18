package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SurveyRequirementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SurveyRequirement getSurveyRequirementSample1() {
        return new SurveyRequirement()
            .id(1L)
            .requirementId(1)
            .requirementDescription("requirementDescription1")
            .surveyId(1)
            .createdByUserCompanyId(1);
    }

    public static SurveyRequirement getSurveyRequirementSample2() {
        return new SurveyRequirement()
            .id(2L)
            .requirementId(2)
            .requirementDescription("requirementDescription2")
            .surveyId(2)
            .createdByUserCompanyId(2);
    }

    public static SurveyRequirement getSurveyRequirementRandomSampleGenerator() {
        return new SurveyRequirement()
            .id(longCount.incrementAndGet())
            .requirementId(intCount.incrementAndGet())
            .requirementDescription(UUID.randomUUID().toString())
            .surveyId(intCount.incrementAndGet())
            .createdByUserCompanyId(intCount.incrementAndGet());
    }
}
