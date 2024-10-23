package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextProductThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextProductTheta getNextProductThetaSample1() {
        return new NextProductTheta().id(1L).name("name1").stock(1);
    }

    public static NextProductTheta getNextProductThetaSample2() {
        return new NextProductTheta().id(2L).name("name2").stock(2);
    }

    public static NextProductTheta getNextProductThetaRandomSampleGenerator() {
        return new NextProductTheta().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).stock(intCount.incrementAndGet());
    }
}
