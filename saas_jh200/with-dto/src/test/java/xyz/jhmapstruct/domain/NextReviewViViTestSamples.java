package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextReviewViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextReviewViVi getNextReviewViViSample1() {
        return new NextReviewViVi().id(1L).rating(1);
    }

    public static NextReviewViVi getNextReviewViViSample2() {
        return new NextReviewViVi().id(2L).rating(2);
    }

    public static NextReviewViVi getNextReviewViViRandomSampleGenerator() {
        return new NextReviewViVi().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
