package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentAlpha getShipmentAlphaSample1() {
        return new ShipmentAlpha().id(1L).trackingNumber("trackingNumber1");
    }

    public static ShipmentAlpha getShipmentAlphaSample2() {
        return new ShipmentAlpha().id(2L).trackingNumber("trackingNumber2");
    }

    public static ShipmentAlpha getShipmentAlphaRandomSampleGenerator() {
        return new ShipmentAlpha().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
