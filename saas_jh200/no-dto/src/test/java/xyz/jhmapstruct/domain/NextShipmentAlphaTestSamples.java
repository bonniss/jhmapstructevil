package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextShipmentAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextShipmentAlpha getNextShipmentAlphaSample1() {
        return new NextShipmentAlpha().id(1L).trackingNumber("trackingNumber1");
    }

    public static NextShipmentAlpha getNextShipmentAlphaSample2() {
        return new NextShipmentAlpha().id(2L).trackingNumber("trackingNumber2");
    }

    public static NextShipmentAlpha getNextShipmentAlphaRandomSampleGenerator() {
        return new NextShipmentAlpha().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
