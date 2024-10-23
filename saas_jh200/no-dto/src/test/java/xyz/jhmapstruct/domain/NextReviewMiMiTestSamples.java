package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextReviewMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextReviewMiMi getNextReviewMiMiSample1() {
        return new NextReviewMiMi().id(1L).rating(1);
    }

    public static NextReviewMiMi getNextReviewMiMiSample2() {
        return new NextReviewMiMi().id(2L).rating(2);
    }

    public static NextReviewMiMi getNextReviewMiMiRandomSampleGenerator() {
        return new NextReviewMiMi().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
