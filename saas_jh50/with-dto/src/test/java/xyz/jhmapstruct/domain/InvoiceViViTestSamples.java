package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceViVi getInvoiceViViSample1() {
        return new InvoiceViVi().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static InvoiceViVi getInvoiceViViSample2() {
        return new InvoiceViVi().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static InvoiceViVi getInvoiceViViRandomSampleGenerator() {
        return new InvoiceViVi().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
