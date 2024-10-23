package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextCategoryMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextCategoryMiMi getNextCategoryMiMiSample1() {
        return new NextCategoryMiMi().id(1L).name("name1").description("description1");
    }

    public static NextCategoryMiMi getNextCategoryMiMiSample2() {
        return new NextCategoryMiMi().id(2L).name("name2").description("description2");
    }

    public static NextCategoryMiMi getNextCategoryMiMiRandomSampleGenerator() {
        return new NextCategoryMiMi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
