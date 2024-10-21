package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlMemTierViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlMemTierVi getAlMemTierViSample1() {
        return new AlMemTierVi().id(1L).name("name1").description("description1").minPoint(1);
    }

    public static AlMemTierVi getAlMemTierViSample2() {
        return new AlMemTierVi().id(2L).name("name2").description("description2").minPoint(2);
    }

    public static AlMemTierVi getAlMemTierViRandomSampleGenerator() {
        return new AlMemTierVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .minPoint(intCount.incrementAndGet());
    }
}
