package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextPaymentBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextPaymentBeta getNextPaymentBetaSample1() {
        return new NextPaymentBeta().id(1L);
    }

    public static NextPaymentBeta getNextPaymentBetaSample2() {
        return new NextPaymentBeta().id(2L);
    }

    public static NextPaymentBeta getNextPaymentBetaRandomSampleGenerator() {
        return new NextPaymentBeta().id(longCount.incrementAndGet());
    }
}
