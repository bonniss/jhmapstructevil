package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategorySigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategorySigma getCategorySigmaSample1() {
        return new CategorySigma().id(1L).name("name1").description("description1");
    }

    public static CategorySigma getCategorySigmaSample2() {
        return new CategorySigma().id(2L).name("name2").description("description2");
    }

    public static CategorySigma getCategorySigmaRandomSampleGenerator() {
        return new CategorySigma()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
