package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextShipmentThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextShipmentTheta getNextShipmentThetaSample1() {
        return new NextShipmentTheta().id(1L).trackingNumber("trackingNumber1");
    }

    public static NextShipmentTheta getNextShipmentThetaSample2() {
        return new NextShipmentTheta().id(2L).trackingNumber("trackingNumber2");
    }

    public static NextShipmentTheta getNextShipmentThetaRandomSampleGenerator() {
        return new NextShipmentTheta().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
