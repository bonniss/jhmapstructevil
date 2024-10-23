package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoryGamma getCategoryGammaSample1() {
        return new CategoryGamma().id(1L).name("name1").description("description1");
    }

    public static CategoryGamma getCategoryGammaSample2() {
        return new CategoryGamma().id(2L).name("name2").description("description2");
    }

    public static CategoryGamma getCategoryGammaRandomSampleGenerator() {
        return new CategoryGamma()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
