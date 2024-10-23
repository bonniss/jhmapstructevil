package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextShipmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextShipment getNextShipmentSample1() {
        return new NextShipment().id(1L).trackingNumber("trackingNumber1");
    }

    public static NextShipment getNextShipmentSample2() {
        return new NextShipment().id(2L).trackingNumber("trackingNumber2");
    }

    public static NextShipment getNextShipmentRandomSampleGenerator() {
        return new NextShipment().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
