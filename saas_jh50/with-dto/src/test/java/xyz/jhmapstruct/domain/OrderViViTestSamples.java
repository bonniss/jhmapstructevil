package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderViVi getOrderViViSample1() {
        return new OrderViVi().id(1L);
    }

    public static OrderViVi getOrderViViSample2() {
        return new OrderViVi().id(2L);
    }

    public static OrderViVi getOrderViViRandomSampleGenerator() {
        return new OrderViVi().id(longCount.incrementAndGet());
    }
}
