package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlSherMaleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlSherMale getAlSherMaleSample1() {
        return new AlSherMale().id(1L).keyword("keyword1").property("property1").title("title1");
    }

    public static AlSherMale getAlSherMaleSample2() {
        return new AlSherMale().id(2L).keyword("keyword2").property("property2").title("title2");
    }

    public static AlSherMale getAlSherMaleRandomSampleGenerator() {
        return new AlSherMale()
            .id(longCount.incrementAndGet())
            .keyword(UUID.randomUUID().toString())
            .property(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString());
    }
}
