package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlVueVueConditionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlVueVueCondition getAlVueVueConditionSample1() {
        return new AlVueVueCondition().id(1L).subject(1L).note("note1");
    }

    public static AlVueVueCondition getAlVueVueConditionSample2() {
        return new AlVueVueCondition().id(2L).subject(2L).note("note2");
    }

    public static AlVueVueCondition getAlVueVueConditionRandomSampleGenerator() {
        return new AlVueVueCondition()
            .id(longCount.incrementAndGet())
            .subject(longCount.incrementAndGet())
            .note(UUID.randomUUID().toString());
    }
}
