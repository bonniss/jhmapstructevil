package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentGamma getShipmentGammaSample1() {
        return new ShipmentGamma().id(1L).trackingNumber("trackingNumber1");
    }

    public static ShipmentGamma getShipmentGammaSample2() {
        return new ShipmentGamma().id(2L).trackingNumber("trackingNumber2");
    }

    public static ShipmentGamma getShipmentGammaRandomSampleGenerator() {
        return new ShipmentGamma().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
