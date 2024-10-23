package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentAlpha getPaymentAlphaSample1() {
        return new PaymentAlpha().id(1L);
    }

    public static PaymentAlpha getPaymentAlphaSample2() {
        return new PaymentAlpha().id(2L);
    }

    public static PaymentAlpha getPaymentAlphaRandomSampleGenerator() {
        return new PaymentAlpha().id(longCount.incrementAndGet());
    }
}
