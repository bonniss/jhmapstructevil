package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextCategoryThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextCategoryTheta getNextCategoryThetaSample1() {
        return new NextCategoryTheta().id(1L).name("name1").description("description1");
    }

    public static NextCategoryTheta getNextCategoryThetaSample2() {
        return new NextCategoryTheta().id(2L).name("name2").description("description2");
    }

    public static NextCategoryTheta getNextCategoryThetaRandomSampleGenerator() {
        return new NextCategoryTheta()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
