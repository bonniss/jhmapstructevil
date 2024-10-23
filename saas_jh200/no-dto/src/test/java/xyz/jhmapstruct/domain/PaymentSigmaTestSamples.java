package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentSigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentSigma getPaymentSigmaSample1() {
        return new PaymentSigma().id(1L);
    }

    public static PaymentSigma getPaymentSigmaSample2() {
        return new PaymentSigma().id(2L);
    }

    public static PaymentSigma getPaymentSigmaRandomSampleGenerator() {
        return new PaymentSigma().id(longCount.incrementAndGet());
    }
}
