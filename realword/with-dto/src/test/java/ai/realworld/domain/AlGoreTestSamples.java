package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlGoreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlGore getAlGoreSample1() {
        return new AlGore().id(1L).name("name1");
    }

    public static AlGore getAlGoreSample2() {
        return new AlGore().id(2L).name("name2");
    }

    public static AlGore getAlGoreRandomSampleGenerator() {
        return new AlGore().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
