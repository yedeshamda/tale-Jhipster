package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NormalUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NormalUser getNormalUserSample1() {
        return new NormalUser()
            .id(1L)
            .userId(1)
            .username("username1")
            .email("email1")
            .firstname("firstname1")
            .lastname("lastname1")
            .age(1)
            .job("job1")
            .gender("gender1")
            .address("address1")
            .earnedPoints(1)
            .status(1);
    }

    public static NormalUser getNormalUserSample2() {
        return new NormalUser()
            .id(2L)
            .userId(2)
            .username("username2")
            .email("email2")
            .firstname("firstname2")
            .lastname("lastname2")
            .age(2)
            .job("job2")
            .gender("gender2")
            .address("address2")
            .earnedPoints(2)
            .status(2);
    }

    public static NormalUser getNormalUserRandomSampleGenerator() {
        return new NormalUser()
            .id(longCount.incrementAndGet())
            .userId(intCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .firstname(UUID.randomUUID().toString())
            .lastname(UUID.randomUUID().toString())
            .age(intCount.incrementAndGet())
            .job(UUID.randomUUID().toString())
            .gender(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .earnedPoints(intCount.incrementAndGet())
            .status(intCount.incrementAndGet());
    }
}
