package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReviewVi getReviewViSample1() {
        return new ReviewVi().id(1L).rating(1);
    }

    public static ReviewVi getReviewViSample2() {
        return new ReviewVi().id(2L).rating(2);
    }

    public static ReviewVi getReviewViRandomSampleGenerator() {
        return new ReviewVi().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
