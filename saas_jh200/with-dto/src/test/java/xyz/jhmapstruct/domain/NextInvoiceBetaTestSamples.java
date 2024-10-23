package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextInvoiceBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextInvoiceBeta getNextInvoiceBetaSample1() {
        return new NextInvoiceBeta().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static NextInvoiceBeta getNextInvoiceBetaSample2() {
        return new NextInvoiceBeta().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static NextInvoiceBeta getNextInvoiceBetaRandomSampleGenerator() {
        return new NextInvoiceBeta().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
