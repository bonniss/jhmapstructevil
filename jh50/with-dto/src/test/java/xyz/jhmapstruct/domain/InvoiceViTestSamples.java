package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceVi getInvoiceViSample1() {
        return new InvoiceVi().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static InvoiceVi getInvoiceViSample2() {
        return new InvoiceVi().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static InvoiceVi getInvoiceViRandomSampleGenerator() {
        return new InvoiceVi().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
