package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewSigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReviewSigma getReviewSigmaSample1() {
        return new ReviewSigma().id(1L).rating(1);
    }

    public static ReviewSigma getReviewSigmaSample2() {
        return new ReviewSigma().id(2L).rating(2);
    }

    public static ReviewSigma getReviewSigmaRandomSampleGenerator() {
        return new ReviewSigma().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
