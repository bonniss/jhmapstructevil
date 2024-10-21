package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlActisoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlActiso getAlActisoSample1() {
        return new AlActiso().id(1L).key("key1").valueJason("valueJason1");
    }

    public static AlActiso getAlActisoSample2() {
        return new AlActiso().id(2L).key("key2").valueJason("valueJason2");
    }

    public static AlActiso getAlActisoRandomSampleGenerator() {
        return new AlActiso().id(longCount.incrementAndGet()).key(UUID.randomUUID().toString()).valueJason(UUID.randomUUID().toString());
    }
}
