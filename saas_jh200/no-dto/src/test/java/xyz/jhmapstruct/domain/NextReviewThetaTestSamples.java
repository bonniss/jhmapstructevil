package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextReviewThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextReviewTheta getNextReviewThetaSample1() {
        return new NextReviewTheta().id(1L).rating(1);
    }

    public static NextReviewTheta getNextReviewThetaSample2() {
        return new NextReviewTheta().id(2L).rating(2);
    }

    public static NextReviewTheta getNextReviewThetaRandomSampleGenerator() {
        return new NextReviewTheta().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
