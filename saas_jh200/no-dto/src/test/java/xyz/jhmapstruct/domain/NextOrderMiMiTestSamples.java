package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextOrderMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextOrderMiMi getNextOrderMiMiSample1() {
        return new NextOrderMiMi().id(1L);
    }

    public static NextOrderMiMi getNextOrderMiMiSample2() {
        return new NextOrderMiMi().id(2L);
    }

    public static NextOrderMiMi getNextOrderMiMiRandomSampleGenerator() {
        return new NextOrderMiMi().id(longCount.incrementAndGet());
    }
}
