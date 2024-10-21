package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentViVi getPaymentViViSample1() {
        return new PaymentViVi().id(1L);
    }

    public static PaymentViVi getPaymentViViSample2() {
        return new PaymentViVi().id(2L);
    }

    public static PaymentViVi getPaymentViViRandomSampleGenerator() {
        return new PaymentViVi().id(longCount.incrementAndGet());
    }
}
