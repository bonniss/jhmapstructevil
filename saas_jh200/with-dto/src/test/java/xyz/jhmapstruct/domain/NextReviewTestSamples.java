package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextReviewTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextReview getNextReviewSample1() {
        return new NextReview().id(1L).rating(1);
    }

    public static NextReview getNextReviewSample2() {
        return new NextReview().id(2L).rating(2);
    }

    public static NextReview getNextReviewRandomSampleGenerator() {
        return new NextReview().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
