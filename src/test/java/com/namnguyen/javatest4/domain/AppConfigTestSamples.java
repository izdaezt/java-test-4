package com.namnguyen.javatest4.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppConfig getAppConfigSample1() {
        return new AppConfig().id(1L).key("key1").value("value1");
    }

    public static AppConfig getAppConfigSample2() {
        return new AppConfig().id(2L).key("key2").value("value2");
    }

    public static AppConfig getAppConfigRandomSampleGenerator() {
        return new AppConfig().id(longCount.incrementAndGet()).key(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
