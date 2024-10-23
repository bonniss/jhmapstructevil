package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextInvoiceViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextInvoiceViVi getNextInvoiceViViSample1() {
        return new NextInvoiceViVi().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static NextInvoiceViVi getNextInvoiceViViSample2() {
        return new NextInvoiceViVi().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static NextInvoiceViVi getNextInvoiceViViRandomSampleGenerator() {
        return new NextInvoiceViVi().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
