package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentMi getShipmentMiSample1() {
        return new ShipmentMi().id(1L).trackingNumber("trackingNumber1");
    }

    public static ShipmentMi getShipmentMiSample2() {
        return new ShipmentMi().id(2L).trackingNumber("trackingNumber2");
    }

    public static ShipmentMi getShipmentMiRandomSampleGenerator() {
        return new ShipmentMi().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
