package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoryViVi getCategoryViViSample1() {
        return new CategoryViVi().id(1L).name("name1").description("description1");
    }

    public static CategoryViVi getCategoryViViSample2() {
        return new CategoryViVi().id(2L).name("name2").description("description2");
    }

    public static CategoryViVi getCategoryViViRandomSampleGenerator() {
        return new CategoryViVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
