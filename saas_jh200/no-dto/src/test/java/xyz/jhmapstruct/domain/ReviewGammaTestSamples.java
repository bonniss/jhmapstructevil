package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReviewGamma getReviewGammaSample1() {
        return new ReviewGamma().id(1L).rating(1);
    }

    public static ReviewGamma getReviewGammaSample2() {
        return new ReviewGamma().id(2L).rating(2);
    }

    public static ReviewGamma getReviewGammaRandomSampleGenerator() {
        return new ReviewGamma().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
