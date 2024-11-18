package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OurDatabasesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OurDatabases getOurDatabasesSample1() {
        return new OurDatabases().id(1L).databaseId(1).name("name1");
    }

    public static OurDatabases getOurDatabasesSample2() {
        return new OurDatabases().id(2L).databaseId(2).name("name2");
    }

    public static OurDatabases getOurDatabasesRandomSampleGenerator() {
        return new OurDatabases().id(longCount.incrementAndGet()).databaseId(intCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
