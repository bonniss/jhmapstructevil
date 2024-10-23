package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextOrderSigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextOrderSigma getNextOrderSigmaSample1() {
        return new NextOrderSigma().id(1L);
    }

    public static NextOrderSigma getNextOrderSigmaSample2() {
        return new NextOrderSigma().id(2L);
    }

    public static NextOrderSigma getNextOrderSigmaRandomSampleGenerator() {
        return new NextOrderSigma().id(longCount.incrementAndGet());
    }
}
