package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderMiMi getOrderMiMiSample1() {
        return new OrderMiMi().id(1L);
    }

    public static OrderMiMi getOrderMiMiSample2() {
        return new OrderMiMi().id(2L);
    }

    public static OrderMiMi getOrderMiMiRandomSampleGenerator() {
        return new OrderMiMi().id(longCount.incrementAndGet());
    }
}
