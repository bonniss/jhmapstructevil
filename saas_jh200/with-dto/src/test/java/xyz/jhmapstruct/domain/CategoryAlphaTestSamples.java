package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoryAlpha getCategoryAlphaSample1() {
        return new CategoryAlpha().id(1L).name("name1").description("description1");
    }

    public static CategoryAlpha getCategoryAlphaSample2() {
        return new CategoryAlpha().id(2L).name("name2").description("description2");
    }

    public static CategoryAlpha getCategoryAlphaRandomSampleGenerator() {
        return new CategoryAlpha()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
