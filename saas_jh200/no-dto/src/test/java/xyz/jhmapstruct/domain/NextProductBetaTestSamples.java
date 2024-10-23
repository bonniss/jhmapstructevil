package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NextProductBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NextProductBeta getNextProductBetaSample1() {
        return new NextProductBeta().id(1L).name("name1").stock(1);
    }

    public static NextProductBeta getNextProductBetaSample2() {
        return new NextProductBeta().id(2L).name("name2").stock(2);
    }

    public static NextProductBeta getNextProductBetaRandomSampleGenerator() {
        return new NextProductBeta().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).stock(intCount.incrementAndGet());
    }
}
