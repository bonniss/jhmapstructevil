package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlPounderViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlPounderVi getAlPounderViSample1() {
        return new AlPounderVi().id(1L).name("name1").weight(1);
    }

    public static AlPounderVi getAlPounderViSample2() {
        return new AlPounderVi().id(2L).name("name2").weight(2);
    }

    public static AlPounderVi getAlPounderViRandomSampleGenerator() {
        return new AlPounderVi().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).weight(intCount.incrementAndGet());
    }
}
