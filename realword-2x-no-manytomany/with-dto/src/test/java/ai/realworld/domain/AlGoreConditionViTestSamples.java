package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlGoreConditionViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlGoreConditionVi getAlGoreConditionViSample1() {
        return new AlGoreConditionVi().id(1L).subject(1L).note("note1");
    }

    public static AlGoreConditionVi getAlGoreConditionViSample2() {
        return new AlGoreConditionVi().id(2L).subject(2L).note("note2");
    }

    public static AlGoreConditionVi getAlGoreConditionViRandomSampleGenerator() {
        return new AlGoreConditionVi()
            .id(longCount.incrementAndGet())
            .subject(longCount.incrementAndGet())
            .note(UUID.randomUUID().toString());
    }
}
