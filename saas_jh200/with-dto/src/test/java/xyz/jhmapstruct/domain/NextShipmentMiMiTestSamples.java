package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextShipmentMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextShipmentMiMi getNextShipmentMiMiSample1() {
        return new NextShipmentMiMi().id(1L).trackingNumber("trackingNumber1");
    }

    public static NextShipmentMiMi getNextShipmentMiMiSample2() {
        return new NextShipmentMiMi().id(2L).trackingNumber("trackingNumber2");
    }

    public static NextShipmentMiMi getNextShipmentMiMiRandomSampleGenerator() {
        return new NextShipmentMiMi().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
