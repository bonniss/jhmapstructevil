package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceAlpha getInvoiceAlphaSample1() {
        return new InvoiceAlpha().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static InvoiceAlpha getInvoiceAlphaSample2() {
        return new InvoiceAlpha().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static InvoiceAlpha getInvoiceAlphaRandomSampleGenerator() {
        return new InvoiceAlpha().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
