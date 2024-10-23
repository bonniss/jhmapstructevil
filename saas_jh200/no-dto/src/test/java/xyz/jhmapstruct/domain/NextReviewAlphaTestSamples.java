package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextReviewAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextReviewAlpha getNextReviewAlphaSample1() {
        return new NextReviewAlpha().id(1L).rating(1);
    }

    public static NextReviewAlpha getNextReviewAlphaSample2() {
        return new NextReviewAlpha().id(2L).rating(2);
    }

    public static NextReviewAlpha getNextReviewAlphaRandomSampleGenerator() {
        return new NextReviewAlpha().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
