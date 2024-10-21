package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentVi getPaymentViSample1() {
        return new PaymentVi().id(1L);
    }

    public static PaymentVi getPaymentViSample2() {
        return new PaymentVi().id(2L);
    }

    public static PaymentVi getPaymentViRandomSampleGenerator() {
        return new PaymentVi().id(longCount.incrementAndGet());
    }
}
