package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextOrderViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextOrderVi getNextOrderViSample1() {
        return new NextOrderVi().id(1L);
    }

    public static NextOrderVi getNextOrderViSample2() {
        return new NextOrderVi().id(2L);
    }

    public static NextOrderVi getNextOrderViRandomSampleGenerator() {
        return new NextOrderVi().id(longCount.incrementAndGet());
    }
}
