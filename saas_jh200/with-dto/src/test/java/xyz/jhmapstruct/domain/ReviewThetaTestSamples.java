package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReviewTheta getReviewThetaSample1() {
        return new ReviewTheta().id(1L).rating(1);
    }

    public static ReviewTheta getReviewThetaSample2() {
        return new ReviewTheta().id(2L).rating(2);
    }

    public static ReviewTheta getReviewThetaRandomSampleGenerator() {
        return new ReviewTheta().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
