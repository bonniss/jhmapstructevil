package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentSigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentSigma getShipmentSigmaSample1() {
        return new ShipmentSigma().id(1L).trackingNumber("trackingNumber1");
    }

    public static ShipmentSigma getShipmentSigmaSample2() {
        return new ShipmentSigma().id(2L).trackingNumber("trackingNumber2");
    }

    public static ShipmentSigma getShipmentSigmaRandomSampleGenerator() {
        return new ShipmentSigma().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
