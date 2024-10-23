package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentTheta getPaymentThetaSample1() {
        return new PaymentTheta().id(1L);
    }

    public static PaymentTheta getPaymentThetaSample2() {
        return new PaymentTheta().id(2L);
    }

    public static PaymentTheta getPaymentThetaRandomSampleGenerator() {
        return new PaymentTheta().id(longCount.incrementAndGet());
    }
}
