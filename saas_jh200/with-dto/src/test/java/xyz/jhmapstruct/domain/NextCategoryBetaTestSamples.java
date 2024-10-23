package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextCategoryBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextCategoryBeta getNextCategoryBetaSample1() {
        return new NextCategoryBeta().id(1L).name("name1").description("description1");
    }

    public static NextCategoryBeta getNextCategoryBetaSample2() {
        return new NextCategoryBeta().id(2L).name("name2").description("description2");
    }

    public static NextCategoryBeta getNextCategoryBetaRandomSampleGenerator() {
        return new NextCategoryBeta()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
