package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextCategoryMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextCategoryMi getNextCategoryMiSample1() {
        return new NextCategoryMi().id(1L).name("name1").description("description1");
    }

    public static NextCategoryMi getNextCategoryMiSample2() {
        return new NextCategoryMi().id(2L).name("name2").description("description2");
    }

    public static NextCategoryMi getNextCategoryMiRandomSampleGenerator() {
        return new NextCategoryMi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
