package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoryBeta getCategoryBetaSample1() {
        return new CategoryBeta().id(1L).name("name1").description("description1");
    }

    public static CategoryBeta getCategoryBetaSample2() {
        return new CategoryBeta().id(2L).name("name2").description("description2");
    }

    public static CategoryBeta getCategoryBetaRandomSampleGenerator() {
        return new CategoryBeta()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
