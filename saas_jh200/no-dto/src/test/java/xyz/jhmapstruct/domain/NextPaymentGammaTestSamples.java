package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextPaymentGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextPaymentGamma getNextPaymentGammaSample1() {
        return new NextPaymentGamma().id(1L);
    }

    public static NextPaymentGamma getNextPaymentGammaSample2() {
        return new NextPaymentGamma().id(2L);
    }

    public static NextPaymentGamma getNextPaymentGammaRandomSampleGenerator() {
        return new NextPaymentGamma().id(longCount.incrementAndGet());
    }
}
