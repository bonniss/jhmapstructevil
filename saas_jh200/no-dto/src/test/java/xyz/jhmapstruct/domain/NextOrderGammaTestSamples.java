package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextOrderGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextOrderGamma getNextOrderGammaSample1() {
        return new NextOrderGamma().id(1L);
    }

    public static NextOrderGamma getNextOrderGammaSample2() {
        return new NextOrderGamma().id(2L);
    }

    public static NextOrderGamma getNextOrderGammaRandomSampleGenerator() {
        return new NextOrderGamma().id(longCount.incrementAndGet());
    }
}
