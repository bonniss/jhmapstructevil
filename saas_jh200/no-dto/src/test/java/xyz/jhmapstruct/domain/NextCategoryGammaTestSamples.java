package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextCategoryGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextCategoryGamma getNextCategoryGammaSample1() {
        return new NextCategoryGamma().id(1L).name("name1").description("description1");
    }

    public static NextCategoryGamma getNextCategoryGammaSample2() {
        return new NextCategoryGamma().id(2L).name("name2").description("description2");
    }

    public static NextCategoryGamma getNextCategoryGammaRandomSampleGenerator() {
        return new NextCategoryGamma()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
