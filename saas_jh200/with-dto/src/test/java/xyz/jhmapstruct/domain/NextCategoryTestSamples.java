package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextCategory getNextCategorySample1() {
        return new NextCategory().id(1L).name("name1").description("description1");
    }

    public static NextCategory getNextCategorySample2() {
        return new NextCategory().id(2L).name("name2").description("description2");
    }

    public static NextCategory getNextCategoryRandomSampleGenerator() {
        return new NextCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
