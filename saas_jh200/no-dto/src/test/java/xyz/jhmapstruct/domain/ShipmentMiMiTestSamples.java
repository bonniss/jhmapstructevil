package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentMiMi getShipmentMiMiSample1() {
        return new ShipmentMiMi().id(1L).trackingNumber("trackingNumber1");
    }

    public static ShipmentMiMi getShipmentMiMiSample2() {
        return new ShipmentMiMi().id(2L).trackingNumber("trackingNumber2");
    }

    public static ShipmentMiMi getShipmentMiMiRandomSampleGenerator() {
        return new ShipmentMiMi().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
