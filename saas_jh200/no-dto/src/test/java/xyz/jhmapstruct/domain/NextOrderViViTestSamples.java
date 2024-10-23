package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextOrderViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextOrderViVi getNextOrderViViSample1() {
        return new NextOrderViVi().id(1L);
    }

    public static NextOrderViVi getNextOrderViViSample2() {
        return new NextOrderViVi().id(2L);
    }

    public static NextOrderViVi getNextOrderViViRandomSampleGenerator() {
        return new NextOrderViVi().id(longCount.incrementAndGet());
    }
}
