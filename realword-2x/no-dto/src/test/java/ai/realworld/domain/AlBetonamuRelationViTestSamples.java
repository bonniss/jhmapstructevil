package ai.realworld.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AlBetonamuRelationViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlBetonamuRelationVi getAlBetonamuRelationViSample1() {
        return new AlBetonamuRelationVi().id(1L);
    }

    public static AlBetonamuRelationVi getAlBetonamuRelationViSample2() {
        return new AlBetonamuRelationVi().id(2L);
    }

    public static AlBetonamuRelationVi getAlBetonamuRelationViRandomSampleGenerator() {
        return new AlBetonamuRelationVi().id(longCount.incrementAndGet());
    }
}
