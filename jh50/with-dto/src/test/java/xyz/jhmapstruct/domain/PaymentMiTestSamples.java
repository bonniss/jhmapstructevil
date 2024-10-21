package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentMi getPaymentMiSample1() {
        return new PaymentMi().id(1L);
    }

    public static PaymentMi getPaymentMiSample2() {
        return new PaymentMi().id(2L);
    }

    public static PaymentMi getPaymentMiRandomSampleGenerator() {
        return new PaymentMi().id(longCount.incrementAndGet());
    }
}
