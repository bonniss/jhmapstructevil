package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentBeta getPaymentBetaSample1() {
        return new PaymentBeta().id(1L);
    }

    public static PaymentBeta getPaymentBetaSample2() {
        return new PaymentBeta().id(2L);
    }

    public static PaymentBeta getPaymentBetaRandomSampleGenerator() {
        return new PaymentBeta().id(longCount.incrementAndGet());
    }
}
