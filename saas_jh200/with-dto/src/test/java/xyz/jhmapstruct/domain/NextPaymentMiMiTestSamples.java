package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextPaymentMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextPaymentMiMi getNextPaymentMiMiSample1() {
        return new NextPaymentMiMi().id(1L);
    }

    public static NextPaymentMiMi getNextPaymentMiMiSample2() {
        return new NextPaymentMiMi().id(2L);
    }

    public static NextPaymentMiMi getNextPaymentMiMiRandomSampleGenerator() {
        return new NextPaymentMiMi().id(longCount.incrementAndGet());
    }
}
