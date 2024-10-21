package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoryMi getCategoryMiSample1() {
        return new CategoryMi().id(1L).name("name1").description("description1");
    }

    public static CategoryMi getCategoryMiSample2() {
        return new CategoryMi().id(2L).name("name2").description("description2");
    }

    public static CategoryMi getCategoryMiRandomSampleGenerator() {
        return new CategoryMi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
