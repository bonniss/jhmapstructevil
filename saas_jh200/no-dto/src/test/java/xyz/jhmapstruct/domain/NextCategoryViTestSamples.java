package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextCategoryViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextCategoryVi getNextCategoryViSample1() {
        return new NextCategoryVi().id(1L).name("name1").description("description1");
    }

    public static NextCategoryVi getNextCategoryViSample2() {
        return new NextCategoryVi().id(2L).name("name2").description("description2");
    }

    public static NextCategoryVi getNextCategoryViRandomSampleGenerator() {
        return new NextCategoryVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
