package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextPaymentViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextPaymentVi getNextPaymentViSample1() {
        return new NextPaymentVi().id(1L);
    }

    public static NextPaymentVi getNextPaymentViSample2() {
        return new NextPaymentVi().id(2L);
    }

    public static NextPaymentVi getNextPaymentViRandomSampleGenerator() {
        return new NextPaymentVi().id(longCount.incrementAndGet());
    }
}
