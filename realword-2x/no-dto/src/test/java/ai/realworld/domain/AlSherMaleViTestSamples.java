package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlSherMaleViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlSherMaleVi getAlSherMaleViSample1() {
        return new AlSherMaleVi().id(1L).keyword("keyword1").property("property1").title("title1");
    }

    public static AlSherMaleVi getAlSherMaleViSample2() {
        return new AlSherMaleVi().id(2L).keyword("keyword2").property("property2").title("title2");
    }

    public static AlSherMaleVi getAlSherMaleViRandomSampleGenerator() {
        return new AlSherMaleVi()
            .id(longCount.incrementAndGet())
            .keyword(UUID.randomUUID().toString())
            .property(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString());
    }
}
