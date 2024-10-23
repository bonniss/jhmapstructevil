package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReviewAlpha getReviewAlphaSample1() {
        return new ReviewAlpha().id(1L).rating(1);
    }

    public static ReviewAlpha getReviewAlphaSample2() {
        return new ReviewAlpha().id(2L).rating(2);
    }

    public static ReviewAlpha getReviewAlphaRandomSampleGenerator() {
        return new ReviewAlpha().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
