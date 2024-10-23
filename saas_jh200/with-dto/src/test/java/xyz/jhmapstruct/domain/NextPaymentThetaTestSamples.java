package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NextPaymentThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextPaymentTheta getNextPaymentThetaSample1() {
        return new NextPaymentTheta().id(1L);
    }

    public static NextPaymentTheta getNextPaymentThetaSample2() {
        return new NextPaymentTheta().id(2L);
    }

    public static NextPaymentTheta getNextPaymentThetaRandomSampleGenerator() {
        return new NextPaymentTheta().id(longCount.incrementAndGet());
    }
}
