package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextShipmentMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextShipmentMi getNextShipmentMiSample1() {
        return new NextShipmentMi().id(1L).trackingNumber("trackingNumber1");
    }

    public static NextShipmentMi getNextShipmentMiSample2() {
        return new NextShipmentMi().id(2L).trackingNumber("trackingNumber2");
    }

    public static NextShipmentMi getNextShipmentMiRandomSampleGenerator() {
        return new NextShipmentMi().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
