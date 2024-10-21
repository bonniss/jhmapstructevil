package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceMi getInvoiceMiSample1() {
        return new InvoiceMi().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static InvoiceMi getInvoiceMiSample2() {
        return new InvoiceMi().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static InvoiceMi getInvoiceMiRandomSampleGenerator() {
        return new InvoiceMi().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
