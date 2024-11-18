package com.tale.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuizProgressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QuizProgress getQuizProgressSample1() {
        return new QuizProgress().id(1L).quizProgressId(1).userId(1).surveyId(1);
    }

    public static QuizProgress getQuizProgressSample2() {
        return new QuizProgress().id(2L).quizProgressId(2).userId(2).surveyId(2);
    }

    public static QuizProgress getQuizProgressRandomSampleGenerator() {
        return new QuizProgress()
            .id(longCount.incrementAndGet())
            .quizProgressId(intCount.incrementAndGet())
            .userId(intCount.incrementAndGet())
            .surveyId(intCount.incrementAndGet());
    }
}
