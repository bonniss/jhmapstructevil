package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextPaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextPayment getNextPaymentSample1() {
        return new NextPayment().id(1L);
    }

    public static NextPayment getNextPaymentSample2() {
        return new NextPayment().id(2L);
    }

    public static NextPayment getNextPaymentRandomSampleGenerator() {
        return new NextPayment().id(longCount.incrementAndGet());
    }
}
