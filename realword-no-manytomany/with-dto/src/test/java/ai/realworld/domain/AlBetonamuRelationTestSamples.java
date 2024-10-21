package ai.realworld.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AlBetonamuRelationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlBetonamuRelation getAlBetonamuRelationSample1() {
        return new AlBetonamuRelation().id(1L);
    }

    public static AlBetonamuRelation getAlBetonamuRelationSample2() {
        return new AlBetonamuRelation().id(2L);
    }

    public static AlBetonamuRelation getAlBetonamuRelationRandomSampleGenerator() {
        return new AlBetonamuRelation().id(longCount.incrementAndGet());
    }
}
