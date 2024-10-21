package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlMemTierTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlMemTier getAlMemTierSample1() {
        return new AlMemTier().id(1L).name("name1").description("description1").minPoint(1);
    }

    public static AlMemTier getAlMemTierSample2() {
        return new AlMemTier().id(2L).name("name2").description("description2").minPoint(2);
    }

    public static AlMemTier getAlMemTierRandomSampleGenerator() {
        return new AlMemTier()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .minPoint(intCount.incrementAndGet());
    }
}
