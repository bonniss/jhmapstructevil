package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextCategoryAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextCategoryAlpha getNextCategoryAlphaSample1() {
        return new NextCategoryAlpha().id(1L).name("name1").description("description1");
    }

    public static NextCategoryAlpha getNextCategoryAlphaSample2() {
        return new NextCategoryAlpha().id(2L).name("name2").description("description2");
    }

    public static NextCategoryAlpha getNextCategoryAlphaRandomSampleGenerator() {
        return new NextCategoryAlpha()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
