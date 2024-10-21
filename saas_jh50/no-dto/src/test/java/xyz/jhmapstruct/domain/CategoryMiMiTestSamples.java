package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoryMiMi getCategoryMiMiSample1() {
        return new CategoryMiMi().id(1L).name("name1").description("description1");
    }

    public static CategoryMiMi getCategoryMiMiSample2() {
        return new CategoryMiMi().id(2L).name("name2").description("description2");
    }

    public static CategoryMiMi getCategoryMiMiRandomSampleGenerator() {
        return new CategoryMiMi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
