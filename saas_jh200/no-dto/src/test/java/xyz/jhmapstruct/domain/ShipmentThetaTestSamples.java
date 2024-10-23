package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentTheta getShipmentThetaSample1() {
        return new ShipmentTheta().id(1L).trackingNumber("trackingNumber1");
    }

    public static ShipmentTheta getShipmentThetaSample2() {
        return new ShipmentTheta().id(2L).trackingNumber("trackingNumber2");
    }

    public static ShipmentTheta getShipmentThetaRandomSampleGenerator() {
        return new ShipmentTheta().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
