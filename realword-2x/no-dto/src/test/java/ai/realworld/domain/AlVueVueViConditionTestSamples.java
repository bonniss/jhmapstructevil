package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlVueVueViConditionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlVueVueViCondition getAlVueVueViConditionSample1() {
        return new AlVueVueViCondition().id(1L).subject(1L).note("note1");
    }

    public static AlVueVueViCondition getAlVueVueViConditionSample2() {
        return new AlVueVueViCondition().id(2L).subject(2L).note("note2");
    }

    public static AlVueVueViCondition getAlVueVueViConditionRandomSampleGenerator() {
        return new AlVueVueViCondition()
            .id(longCount.incrementAndGet())
            .subject(longCount.incrementAndGet())
            .note(UUID.randomUUID().toString());
    }
}
