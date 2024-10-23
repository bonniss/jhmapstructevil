package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextShipmentBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextShipmentBeta getNextShipmentBetaSample1() {
        return new NextShipmentBeta().id(1L).trackingNumber("trackingNumber1");
    }

    public static NextShipmentBeta getNextShipmentBetaSample2() {
        return new NextShipmentBeta().id(2L).trackingNumber("trackingNumber2");
    }

    public static NextShipmentBeta getNextShipmentBetaRandomSampleGenerator() {
        return new NextShipmentBeta().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
