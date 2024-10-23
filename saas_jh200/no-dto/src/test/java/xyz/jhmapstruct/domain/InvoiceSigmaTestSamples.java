package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceSigmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceSigma getInvoiceSigmaSample1() {
        return new InvoiceSigma().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static InvoiceSigma getInvoiceSigmaSample2() {
        return new InvoiceSigma().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static InvoiceSigma getInvoiceSigmaRandomSampleGenerator() {
        return new InvoiceSigma().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
