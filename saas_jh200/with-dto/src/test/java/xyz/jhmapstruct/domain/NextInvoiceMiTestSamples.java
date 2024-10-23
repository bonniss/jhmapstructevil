package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextInvoiceMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextInvoiceMi getNextInvoiceMiSample1() {
        return new NextInvoiceMi().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static NextInvoiceMi getNextInvoiceMiSample2() {
        return new NextInvoiceMi().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static NextInvoiceMi getNextInvoiceMiRandomSampleGenerator() {
        return new NextInvoiceMi().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
