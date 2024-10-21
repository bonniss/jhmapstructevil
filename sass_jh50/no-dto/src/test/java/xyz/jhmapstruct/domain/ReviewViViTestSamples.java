package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReviewViVi getReviewViViSample1() {
        return new ReviewViVi().id(1L).rating(1);
    }

    public static ReviewViVi getReviewViViSample2() {
        return new ReviewViVi().id(2L).rating(2);
    }

    public static ReviewViVi getReviewViViRandomSampleGenerator() {
        return new ReviewViVi().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}