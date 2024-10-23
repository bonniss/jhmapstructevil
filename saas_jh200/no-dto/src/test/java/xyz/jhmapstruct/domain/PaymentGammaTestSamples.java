package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentGamma getPaymentGammaSample1() {
        return new PaymentGamma().id(1L);
    }

    public static PaymentGamma getPaymentGammaSample2() {
        return new PaymentGamma().id(2L);
    }

    public static PaymentGamma getPaymentGammaRandomSampleGenerator() {
        return new PaymentGamma().id(longCount.incrementAndGet());
    }
}
