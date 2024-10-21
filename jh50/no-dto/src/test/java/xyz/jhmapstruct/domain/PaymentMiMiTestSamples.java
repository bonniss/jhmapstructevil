package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentMiMi getPaymentMiMiSample1() {
        return new PaymentMiMi().id(1L);
    }

    public static PaymentMiMi getPaymentMiMiSample2() {
        return new PaymentMiMi().id(2L);
    }

    public static PaymentMiMi getPaymentMiMiRandomSampleGenerator() {
        return new PaymentMiMi().id(longCount.incrementAndGet());
    }
}
