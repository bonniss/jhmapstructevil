package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlGoreConditionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlGoreCondition getAlGoreConditionSample1() {
        return new AlGoreCondition().id(1L).subject(1L).note("note1");
    }

    public static AlGoreCondition getAlGoreConditionSample2() {
        return new AlGoreCondition().id(2L).subject(2L).note("note2");
    }

    public static AlGoreCondition getAlGoreConditionRandomSampleGenerator() {
        return new AlGoreCondition()
            .id(longCount.incrementAndGet())
            .subject(longCount.incrementAndGet())
            .note(UUID.randomUUID().toString());
    }
}
