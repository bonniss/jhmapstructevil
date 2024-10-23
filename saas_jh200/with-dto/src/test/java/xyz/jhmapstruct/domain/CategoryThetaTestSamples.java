package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoryTheta getCategoryThetaSample1() {
        return new CategoryTheta().id(1L).name("name1").description("description1");
    }

    public static CategoryTheta getCategoryThetaSample2() {
        return new CategoryTheta().id(2L).name("name2").description("description2");
    }

    public static CategoryTheta getCategoryThetaRandomSampleGenerator() {
        return new CategoryTheta()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
