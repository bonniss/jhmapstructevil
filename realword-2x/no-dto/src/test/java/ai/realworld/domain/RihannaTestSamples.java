package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RihannaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Rihanna getRihannaSample1() {
        return new Rihanna().id(1L).name("name1").description("description1").permissionGridJason("permissionGridJason1");
    }

    public static Rihanna getRihannaSample2() {
        return new Rihanna().id(2L).name("name2").description("description2").permissionGridJason("permissionGridJason2");
    }

    public static Rihanna getRihannaRandomSampleGenerator() {
        return new Rihanna()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .permissionGridJason(UUID.randomUUID().toString());
    }
}
