package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceBeta getInvoiceBetaSample1() {
        return new InvoiceBeta().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static InvoiceBeta getInvoiceBetaSample2() {
        return new InvoiceBeta().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static InvoiceBeta getInvoiceBetaRandomSampleGenerator() {
        return new InvoiceBeta().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
