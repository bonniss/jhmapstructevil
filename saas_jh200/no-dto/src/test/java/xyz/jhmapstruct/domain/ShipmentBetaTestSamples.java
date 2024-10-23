package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentBeta getShipmentBetaSample1() {
        return new ShipmentBeta().id(1L).trackingNumber("trackingNumber1");
    }

    public static ShipmentBeta getShipmentBetaSample2() {
        return new ShipmentBeta().id(2L).trackingNumber("trackingNumber2");
    }

    public static ShipmentBeta getShipmentBetaRandomSampleGenerator() {
        return new ShipmentBeta().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
