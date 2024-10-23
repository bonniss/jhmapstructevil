package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextShipmentViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextShipmentVi getNextShipmentViSample1() {
        return new NextShipmentVi().id(1L).trackingNumber("trackingNumber1");
    }

    public static NextShipmentVi getNextShipmentViSample2() {
        return new NextShipmentVi().id(2L).trackingNumber("trackingNumber2");
    }

    public static NextShipmentVi getNextShipmentViRandomSampleGenerator() {
        return new NextShipmentVi().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
