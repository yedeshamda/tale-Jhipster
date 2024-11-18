package com.tale.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SurveyParticipantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SurveyParticipant getSurveyParticipantSample1() {
        return new SurveyParticipant().id(1L).participantId(1);
    }

    public static SurveyParticipant getSurveyParticipantSample2() {
        return new SurveyParticipant().id(2L).participantId(2);
    }

    public static SurveyParticipant getSurveyParticipantRandomSampleGenerator() {
        return new SurveyParticipant().id(longCount.incrementAndGet()).participantId(intCount.incrementAndGet());
    }
}
