package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextOrderAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextOrderAlpha getNextOrderAlphaSample1() {
        return new NextOrderAlpha().id(1L);
    }

    public static NextOrderAlpha getNextOrderAlphaSample2() {
        return new NextOrderAlpha().id(2L);
    }

    public static NextOrderAlpha getNextOrderAlphaRandomSampleGenerator() {
        return new NextOrderAlpha().id(longCount.incrementAndGet());
    }
}
