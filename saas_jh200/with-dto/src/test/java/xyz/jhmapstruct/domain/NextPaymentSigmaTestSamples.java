package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextPaymentSigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextPaymentSigma getNextPaymentSigmaSample1() {
        return new NextPaymentSigma().id(1L);
    }

    public static NextPaymentSigma getNextPaymentSigmaSample2() {
        return new NextPaymentSigma().id(2L);
    }

    public static NextPaymentSigma getNextPaymentSigmaRandomSampleGenerator() {
        return new NextPaymentSigma().id(longCount.incrementAndGet());
    }
}
