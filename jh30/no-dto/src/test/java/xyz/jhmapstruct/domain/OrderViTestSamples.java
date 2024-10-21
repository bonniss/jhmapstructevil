package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderVi getOrderViSample1() {
        return new OrderVi().id(1L);
    }

    public static OrderVi getOrderViSample2() {
        return new OrderVi().id(2L);
    }

    public static OrderVi getOrderViRandomSampleGenerator() {
        return new OrderVi().id(longCount.incrementAndGet());
    }
}
