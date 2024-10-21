package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceMiMi getInvoiceMiMiSample1() {
        return new InvoiceMiMi().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static InvoiceMiMi getInvoiceMiMiSample2() {
        return new InvoiceMiMi().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static InvoiceMiMi getInvoiceMiMiRandomSampleGenerator() {
        return new InvoiceMiMi().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
