package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextOrderThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextOrderTheta getNextOrderThetaSample1() {
        return new NextOrderTheta().id(1L);
    }

    public static NextOrderTheta getNextOrderThetaSample2() {
        return new NextOrderTheta().id(2L);
    }

    public static NextOrderTheta getNextOrderThetaRandomSampleGenerator() {
        return new NextOrderTheta().id(longCount.incrementAndGet());
    }
}
