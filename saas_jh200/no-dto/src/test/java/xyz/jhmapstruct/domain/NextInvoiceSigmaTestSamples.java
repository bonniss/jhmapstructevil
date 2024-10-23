package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextInvoiceSigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextInvoiceSigma getNextInvoiceSigmaSample1() {
        return new NextInvoiceSigma().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static NextInvoiceSigma getNextInvoiceSigmaSample2() {
        return new NextInvoiceSigma().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static NextInvoiceSigma getNextInvoiceSigmaRandomSampleGenerator() {
        return new NextInvoiceSigma().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
