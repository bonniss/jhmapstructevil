package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReviewMiMi getReviewMiMiSample1() {
        return new ReviewMiMi().id(1L).rating(1);
    }

    public static ReviewMiMi getReviewMiMiSample2() {
        return new ReviewMiMi().id(2L).rating(2);
    }

    public static ReviewMiMi getReviewMiMiRandomSampleGenerator() {
        return new ReviewMiMi().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
