package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderAlpha getOrderAlphaSample1() {
        return new OrderAlpha().id(1L);
    }

    public static OrderAlpha getOrderAlphaSample2() {
        return new OrderAlpha().id(2L);
    }

    public static OrderAlpha getOrderAlphaRandomSampleGenerator() {
        return new OrderAlpha().id(longCount.incrementAndGet());
    }
}
