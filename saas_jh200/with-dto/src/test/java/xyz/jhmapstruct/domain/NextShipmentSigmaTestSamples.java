package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextShipmentSigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextShipmentSigma getNextShipmentSigmaSample1() {
        return new NextShipmentSigma().id(1L).trackingNumber("trackingNumber1");
    }

    public static NextShipmentSigma getNextShipmentSigmaSample2() {
        return new NextShipmentSigma().id(2L).trackingNumber("trackingNumber2");
    }

    public static NextShipmentSigma getNextShipmentSigmaRandomSampleGenerator() {
        return new NextShipmentSigma().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
