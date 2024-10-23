package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextReviewViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextReviewVi getNextReviewViSample1() {
        return new NextReviewVi().id(1L).rating(1);
    }

    public static NextReviewVi getNextReviewViSample2() {
        return new NextReviewVi().id(2L).rating(2);
    }

    public static NextReviewVi getNextReviewViRandomSampleGenerator() {
        return new NextReviewVi().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
