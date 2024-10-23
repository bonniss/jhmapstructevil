package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceTheta getInvoiceThetaSample1() {
        return new InvoiceTheta().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static InvoiceTheta getInvoiceThetaSample2() {
        return new InvoiceTheta().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static InvoiceTheta getInvoiceThetaRandomSampleGenerator() {
        return new InvoiceTheta().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
