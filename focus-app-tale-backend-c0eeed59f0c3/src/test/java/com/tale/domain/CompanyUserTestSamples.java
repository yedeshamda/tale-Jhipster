package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompanyUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CompanyUser getCompanyUserSample1() {
        return new CompanyUser()
            .id(1L)
            .companyUserId(1)
            .username("username1")
            .password("password1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .firstName("firstName1")
            .lastName("lastName1")
            .jobTitle("jobTitle1")
            .status(1);
    }

    public static CompanyUser getCompanyUserSample2() {
        return new CompanyUser()
            .id(2L)
            .companyUserId(2)
            .username("username2")
            .password("password2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .firstName("firstName2")
            .lastName("lastName2")
            .jobTitle("jobTitle2")
            .status(2);
    }

    public static CompanyUser getCompanyUserRandomSampleGenerator() {
        return new CompanyUser()
            .id(longCount.incrementAndGet())
            .companyUserId(intCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .jobTitle(UUID.randomUUID().toString())
            .status(intCount.incrementAndGet());
    }
}
