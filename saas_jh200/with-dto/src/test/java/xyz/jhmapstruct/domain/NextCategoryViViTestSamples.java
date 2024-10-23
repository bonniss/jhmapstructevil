package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextCategoryViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextCategoryViVi getNextCategoryViViSample1() {
        return new NextCategoryViVi().id(1L).name("name1").description("description1");
    }

    public static NextCategoryViVi getNextCategoryViViSample2() {
        return new NextCategoryViVi().id(2L).name("name2").description("description2");
    }

    public static NextCategoryViVi getNextCategoryViViRandomSampleGenerator() {
        return new NextCategoryViVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
