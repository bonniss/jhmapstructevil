package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextShipmentGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextShipmentGamma getNextShipmentGammaSample1() {
        return new NextShipmentGamma().id(1L).trackingNumber("trackingNumber1");
    }

    public static NextShipmentGamma getNextShipmentGammaSample2() {
        return new NextShipmentGamma().id(2L).trackingNumber("trackingNumber2");
    }

    public static NextShipmentGamma getNextShipmentGammaRandomSampleGenerator() {
        return new NextShipmentGamma().id(longCount.incrementAndGet()).trackingNumber(UUID.randomUUID().toString());
    }
}
