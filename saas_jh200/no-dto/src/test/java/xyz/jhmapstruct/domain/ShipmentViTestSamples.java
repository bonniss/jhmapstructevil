package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentVi getShipmentViSample1() {
        return new ShipmentVi().id(1L).trackingNumber("trackingNumber1");
    }

    public static ShipmentVi getShipmentViSample2() {
        return new ShipmentVi().id(2L).trackingNumber("trackingNumber2");
    }

    public static ShipmentVi getShipmentViRandomSampleGenerator() {
        return new ShipmentVi().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
