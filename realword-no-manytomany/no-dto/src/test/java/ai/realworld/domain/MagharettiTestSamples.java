package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MagharettiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Magharetti getMagharettiSample1() {
        return new Magharetti().id(1L).name("name1").label("label1");
    }

    public static Magharetti getMagharettiSample2() {
        return new Magharetti().id(2L).name("name2").label("label2");
    }

    public static Magharetti getMagharettiRandomSampleGenerator() {
        return new Magharetti().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).label(UUID.randomUUID().toString());
    }
}
