package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SaisanCogViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SaisanCogVi getSaisanCogViSample1() {
        return new SaisanCogVi().id(1L).key("key1").valueJason("valueJason1");
    }

    public static SaisanCogVi getSaisanCogViSample2() {
        return new SaisanCogVi().id(2L).key("key2").valueJason("valueJason2");
    }

    public static SaisanCogVi getSaisanCogViRandomSampleGenerator() {
        return new SaisanCogVi().id(longCount.incrementAndGet()).key(UUID.randomUUID().toString()).valueJason(UUID.randomUUID().toString());
    }
}
