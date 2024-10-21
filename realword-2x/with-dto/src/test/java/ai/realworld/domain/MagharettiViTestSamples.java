package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MagharettiViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MagharettiVi getMagharettiViSample1() {
        return new MagharettiVi().id(1L).name("name1").label("label1");
    }

    public static MagharettiVi getMagharettiViSample2() {
        return new MagharettiVi().id(2L).name("name2").label("label2");
    }

    public static MagharettiVi getMagharettiViRandomSampleGenerator() {
        return new MagharettiVi().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).label(UUID.randomUUID().toString());
    }
}
