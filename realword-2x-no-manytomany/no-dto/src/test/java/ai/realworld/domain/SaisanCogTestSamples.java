package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SaisanCogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SaisanCog getSaisanCogSample1() {
        return new SaisanCog().id(1L).key("key1").valueJason("valueJason1");
    }

    public static SaisanCog getSaisanCogSample2() {
        return new SaisanCog().id(2L).key("key2").valueJason("valueJason2");
    }

    public static SaisanCog getSaisanCogRandomSampleGenerator() {
        return new SaisanCog().id(longCount.incrementAndGet()).key(UUID.randomUUID().toString()).valueJason(UUID.randomUUID().toString());
    }
}
