package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextPaymentAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextPaymentAlpha getNextPaymentAlphaSample1() {
        return new NextPaymentAlpha().id(1L);
    }

    public static NextPaymentAlpha getNextPaymentAlphaSample2() {
        return new NextPaymentAlpha().id(2L);
    }

    public static NextPaymentAlpha getNextPaymentAlphaRandomSampleGenerator() {
        return new NextPaymentAlpha().id(longCount.incrementAndGet());
    }
}
