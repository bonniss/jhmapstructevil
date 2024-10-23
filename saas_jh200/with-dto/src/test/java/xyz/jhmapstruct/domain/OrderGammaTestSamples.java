package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderGamma getOrderGammaSample1() {
        return new OrderGamma().id(1L);
    }

    public static OrderGamma getOrderGammaSample2() {
        return new OrderGamma().id(2L);
    }

    public static OrderGamma getOrderGammaRandomSampleGenerator() {
        return new OrderGamma().id(longCount.incrementAndGet());
    }
}
