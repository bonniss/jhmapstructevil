package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReviewMi getReviewMiSample1() {
        return new ReviewMi().id(1L).rating(1);
    }

    public static ReviewMi getReviewMiSample2() {
        return new ReviewMi().id(2L).rating(2);
    }

    public static ReviewMi getReviewMiRandomSampleGenerator() {
        return new ReviewMi().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
