package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextInvoiceGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextInvoiceGamma getNextInvoiceGammaSample1() {
        return new NextInvoiceGamma().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static NextInvoiceGamma getNextInvoiceGammaSample2() {
        return new NextInvoiceGamma().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static NextInvoiceGamma getNextInvoiceGammaRandomSampleGenerator() {
        return new NextInvoiceGamma().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
