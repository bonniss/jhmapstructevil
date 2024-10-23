package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextInvoiceAlphaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextInvoiceAlpha getNextInvoiceAlphaSample1() {
        return new NextInvoiceAlpha().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static NextInvoiceAlpha getNextInvoiceAlphaSample2() {
        return new NextInvoiceAlpha().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static NextInvoiceAlpha getNextInvoiceAlphaRandomSampleGenerator() {
        return new NextInvoiceAlpha().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
