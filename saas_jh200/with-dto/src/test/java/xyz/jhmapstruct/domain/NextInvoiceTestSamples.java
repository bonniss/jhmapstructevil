package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextInvoiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextInvoice getNextInvoiceSample1() {
        return new NextInvoice().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static NextInvoice getNextInvoiceSample2() {
        return new NextInvoice().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static NextInvoice getNextInvoiceRandomSampleGenerator() {
        return new NextInvoice().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
