package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextOrder getNextOrderSample1() {
        return new NextOrder().id(1L);
    }

    public static NextOrder getNextOrderSample2() {
        return new NextOrder().id(2L);
    }

    public static NextOrder getNextOrderRandomSampleGenerator() {
        return new NextOrder().id(longCount.incrementAndGet());
    }
}
