package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextInvoiceMiMiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextInvoiceMiMi getNextInvoiceMiMiSample1() {
        return new NextInvoiceMiMi().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static NextInvoiceMiMi getNextInvoiceMiMiSample2() {
        return new NextInvoiceMiMi().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static NextInvoiceMiMi getNextInvoiceMiMiRandomSampleGenerator() {
        return new NextInvoiceMiMi().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
