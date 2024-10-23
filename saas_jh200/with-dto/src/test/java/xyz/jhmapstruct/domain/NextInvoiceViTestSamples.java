package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextInvoiceViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextInvoiceVi getNextInvoiceViSample1() {
        return new NextInvoiceVi().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static NextInvoiceVi getNextInvoiceViSample2() {
        return new NextInvoiceVi().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static NextInvoiceVi getNextInvoiceViRandomSampleGenerator() {
        return new NextInvoiceVi().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
