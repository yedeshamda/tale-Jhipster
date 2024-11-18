package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserCompanyRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserCompanyRole getUserCompanyRoleSample1() {
        return new UserCompanyRole().id(1L).roleId(1).roleName("roleName1").description("description1").status(1);
    }

    public static UserCompanyRole getUserCompanyRoleSample2() {
        return new UserCompanyRole().id(2L).roleId(2).roleName("roleName2").description("description2").status(2);
    }

    public static UserCompanyRole getUserCompanyRoleRandomSampleGenerator() {
        return new UserCompanyRole()
            .id(longCount.incrementAndGet())
            .roleId(intCount.incrementAndGet())
            .roleName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .status(intCount.incrementAndGet());
    }
}
