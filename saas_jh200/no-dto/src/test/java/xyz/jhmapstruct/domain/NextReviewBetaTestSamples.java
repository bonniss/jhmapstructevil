package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextReviewBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextReviewBeta getNextReviewBetaSample1() {
        return new NextReviewBeta().id(1L).rating(1);
    }

    public static NextReviewBeta getNextReviewBetaSample2() {
        return new NextReviewBeta().id(2L).rating(2);
    }

    public static NextReviewBeta getNextReviewBetaRandomSampleGenerator() {
        return new NextReviewBeta().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
