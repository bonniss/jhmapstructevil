package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlActisoViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlActisoVi getAlActisoViSample1() {
        return new AlActisoVi().id(1L).key("key1").valueJason("valueJason1");
    }

    public static AlActisoVi getAlActisoViSample2() {
        return new AlActisoVi().id(2L).key("key2").valueJason("valueJason2");
    }

    public static AlActisoVi getAlActisoViRandomSampleGenerator() {
        return new AlActisoVi().id(longCount.incrementAndGet()).key(UUID.randomUUID().toString()).valueJason(UUID.randomUUID().toString());
    }
}
