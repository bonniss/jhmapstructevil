package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderTheta getOrderThetaSample1() {
        return new OrderTheta().id(1L);
    }

    public static OrderTheta getOrderThetaSample2() {
        return new OrderTheta().id(2L);
    }

    public static OrderTheta getOrderThetaRandomSampleGenerator() {
        return new OrderTheta().id(longCount.incrementAndGet());
    }
}
