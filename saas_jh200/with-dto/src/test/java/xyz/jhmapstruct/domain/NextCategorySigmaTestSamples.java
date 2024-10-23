package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextCategorySigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextCategorySigma getNextCategorySigmaSample1() {
        return new NextCategorySigma().id(1L).name("name1").description("description1");
    }

    public static NextCategorySigma getNextCategorySigmaSample2() {
        return new NextCategorySigma().id(2L).name("name2").description("description2");
    }

    public static NextCategorySigma getNextCategorySigmaRandomSampleGenerator() {
        return new NextCategorySigma()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
