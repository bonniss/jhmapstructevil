package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderSigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderSigma getOrderSigmaSample1() {
        return new OrderSigma().id(1L);
    }

    public static OrderSigma getOrderSigmaSample2() {
        return new OrderSigma().id(2L);
    }

    public static OrderSigma getOrderSigmaRandomSampleGenerator() {
        return new OrderSigma().id(longCount.incrementAndGet());
    }
}
