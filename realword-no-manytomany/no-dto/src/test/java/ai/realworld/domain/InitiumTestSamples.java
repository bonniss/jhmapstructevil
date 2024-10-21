package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InitiumTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Initium getInitiumSample1() {
        return new Initium().id(1L).name("name1").slug("slug1").description("description1");
    }

    public static Initium getInitiumSample2() {
        return new Initium().id(2L).name("name2").slug("slug2").description("description2");
    }

    public static Initium getInitiumRandomSampleGenerator() {
        return new Initium()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
