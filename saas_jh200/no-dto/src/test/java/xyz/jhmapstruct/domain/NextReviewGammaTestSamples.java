package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextReviewGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextReviewGamma getNextReviewGammaSample1() {
        return new NextReviewGamma().id(1L).rating(1);
    }

    public static NextReviewGamma getNextReviewGammaSample2() {
        return new NextReviewGamma().id(2L).rating(2);
    }

    public static NextReviewGamma getNextReviewGammaRandomSampleGenerator() {
        return new NextReviewGamma().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
