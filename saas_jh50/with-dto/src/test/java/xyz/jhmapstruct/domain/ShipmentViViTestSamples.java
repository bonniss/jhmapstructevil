package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentViVi getShipmentViViSample1() {
        return new ShipmentViVi().id(1L).trackingNumber("trackingNumber1");
    }

    public static ShipmentViVi getShipmentViViSample2() {
        return new ShipmentViVi().id(2L).trackingNumber("trackingNumber2");
    }

    public static ShipmentViVi getShipmentViViRandomSampleGenerator() {
        return new ShipmentViVi().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
