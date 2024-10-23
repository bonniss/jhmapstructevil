package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceGammaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceGamma getInvoiceGammaSample1() {
        return new InvoiceGamma().id(1L).invoiceNumber("invoiceNumber1");
    }

    public static InvoiceGamma getInvoiceGammaSample2() {
        return new InvoiceGamma().id(2L).invoiceNumber("invoiceNumber2");
    }

    public static InvoiceGamma getInvoiceGammaRandomSampleGenerator() {
        return new InvoiceGamma().id(longCount.incrementAndGet()).invoiceNumber(UUID.randomUUID().toString());
    }
}
