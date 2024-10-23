package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderBeta getOrderBetaSample1() {
        return new OrderBeta().id(1L);
    }

    public static OrderBeta getOrderBetaSample2() {
        return new OrderBeta().id(2L);
    }

    public static OrderBeta getOrderBetaRandomSampleGenerator() {
        return new OrderBeta().id(longCount.incrementAndGet());
    }
}
