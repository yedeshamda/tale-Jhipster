package com.tale.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CampaignTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Campaign getCampaignSample1() {
        return new Campaign().id(1L).campaignId(1).title("title1");
    }

    public static Campaign getCampaignSample2() {
        return new Campaign().id(2L).campaignId(2).title("title2");
    }

    public static Campaign getCampaignRandomSampleGenerator() {
        return new Campaign().id(longCount.incrementAndGet()).campaignId(intCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
