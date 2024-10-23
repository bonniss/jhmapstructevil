package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextPaymentViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextPaymentViVi getNextPaymentViViSample1() {
        return new NextPaymentViVi().id(1L);
    }

    public static NextPaymentViVi getNextPaymentViViSample2() {
        return new NextPaymentViVi().id(2L);
    }

    public static NextPaymentViVi getNextPaymentViViRandomSampleGenerator() {
        return new NextPaymentViVi().id(longCount.incrementAndGet());
    }
}
