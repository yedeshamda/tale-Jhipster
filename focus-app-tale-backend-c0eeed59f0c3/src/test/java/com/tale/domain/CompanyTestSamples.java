package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompanyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Company getCompanySample1() {
        return new Company()
            .id(1L)
            .companyId(1)
            .companyName("companyName1")
            .industry("industry1")
            .numberOfEmployees(1)
            .address("address1")
            .website("website1")
            .status(1);
    }

    public static Company getCompanySample2() {
        return new Company()
            .id(2L)
            .companyId(2)
            .companyName("companyName2")
            .industry("industry2")
            .numberOfEmployees(2)
            .address("address2")
            .website("website2")
            .status(2);
    }

    public static Company getCompanyRandomSampleGenerator() {
        return new Company()
            .id(longCount.incrementAndGet())
            .companyId(intCount.incrementAndGet())
            .companyName(UUID.randomUUID().toString())
            .industry(UUID.randomUUID().toString())
            .numberOfEmployees(intCount.incrementAndGet())
            .address(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .status(intCount.incrementAndGet());
    }
}
