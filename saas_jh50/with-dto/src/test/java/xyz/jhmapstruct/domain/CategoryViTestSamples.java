package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoryVi getCategoryViSample1() {
        return new CategoryVi().id(1L).name("name1").description("description1");
    }

    public static CategoryVi getCategoryViSample2() {
        return new CategoryVi().id(2L).name("name2").description("description2");
    }

    public static CategoryVi getCategoryViRandomSampleGenerator() {
        return new CategoryVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
