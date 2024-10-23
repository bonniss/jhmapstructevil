package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextPaymentMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextPaymentMi getNextPaymentMiSample1() {
        return new NextPaymentMi().id(1L);
    }

    public static NextPaymentMi getNextPaymentMiSample2() {
        return new NextPaymentMi().id(2L);
    }

    public static NextPaymentMi getNextPaymentMiRandomSampleGenerator() {
        return new NextPaymentMi().id(longCount.incrementAndGet());
    }
}
