package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextShipmentViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextShipmentViVi getNextShipmentViViSample1() {
        return new NextShipmentViVi().id(1L).trackingNumber("trackingNumber1");
    }

    public static NextShipmentViVi getNextShipmentViViSample2() {
        return new NextShipmentViVi().id(2L).trackingNumber("trackingNumber2");
    }

    public static NextShipmentViVi getNextShipmentViViRandomSampleGenerator() {
        return new NextShipmentViVi().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
