package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextInvoiceThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextInvoiceTheta getNextInvoiceThetaSample1() {
        return new NextInvoiceTheta().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static NextInvoiceTheta getNextInvoiceThetaSample2() {
        return new NextInvoiceTheta().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static NextInvoiceTheta getNextInvoiceThetaRandomSampleGenerator() {
        return new NextInvoiceTheta().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
