package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextReviewSigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextReviewSigma getNextReviewSigmaSample1() {
        return new NextReviewSigma().id(1L).rating(1);
    }

    public static NextReviewSigma getNextReviewSigmaSample2() {
        return new NextReviewSigma().id(2L).rating(2);
    }

    public static NextReviewSigma getNextReviewSigmaRandomSampleGenerator() {
        return new NextReviewSigma().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
