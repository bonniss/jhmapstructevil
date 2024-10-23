package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReviewBeta getReviewBetaSample1() {
        return new ReviewBeta().id(1L).rating(1);
    }

    public static ReviewBeta getReviewBetaSample2() {
        return new ReviewBeta().id(2L).rating(2);
    }

    public static ReviewBeta getReviewBetaRandomSampleGenerator() {
        return new ReviewBeta().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
