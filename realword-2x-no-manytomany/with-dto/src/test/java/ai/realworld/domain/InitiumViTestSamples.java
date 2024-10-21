package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InitiumViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InitiumVi getInitiumViSample1() {
        return new InitiumVi().id(1L).name("name1").slug("slug1").description("description1");
    }

    public static InitiumVi getInitiumViSample2() {
        return new InitiumVi().id(2L).name("name2").slug("slug2").description("description2");
    }

    public static InitiumVi getInitiumViRandomSampleGenerator() {
        return new InitiumVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
