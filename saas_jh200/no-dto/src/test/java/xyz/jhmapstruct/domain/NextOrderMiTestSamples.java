package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextOrderMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextOrderMi getNextOrderMiSample1() {
        return new NextOrderMi().id(1L);
    }

    public static NextOrderMi getNextOrderMiSample2() {
        return new NextOrderMi().id(2L);
    }

    public static NextOrderMi getNextOrderMiRandomSampleGenerator() {
        return new NextOrderMi().id(longCount.incrementAndGet());
    }
}
