package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextReviewMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextReviewMi getNextReviewMiSample1() {
        return new NextReviewMi().id(1L).rating(1);
    }

    public static NextReviewMi getNextReviewMiSample2() {
        return new NextReviewMi().id(2L).rating(2);
    }

    public static NextReviewMi getNextReviewMiRandomSampleGenerator() {
        return new NextReviewMi().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
