package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextOrderBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextOrderBeta getNextOrderBetaSample1() {
        return new NextOrderBeta().id(1L);
    }

    public static NextOrderBeta getNextOrderBetaSample2() {
        return new NextOrderBeta().id(2L);
    }

    public static NextOrderBeta getNextOrderBetaRandomSampleGenerator() {
        return new NextOrderBeta().id(longCount.incrementAndGet());
    }
}
