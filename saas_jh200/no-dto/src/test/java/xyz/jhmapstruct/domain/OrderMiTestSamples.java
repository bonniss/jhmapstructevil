package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderMi getOrderMiSample1() {
        return new OrderMi().id(1L);
    }

    public static OrderMi getOrderMiSample2() {
        return new OrderMi().id(2L);
    }

    public static OrderMi getOrderMiRandomSampleGenerator() {
        return new OrderMi().id(longCount.incrementAndGet());
    }
}
