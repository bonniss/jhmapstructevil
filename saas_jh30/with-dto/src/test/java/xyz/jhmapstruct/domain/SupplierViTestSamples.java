package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SupplierViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SupplierVi getSupplierViSample1() {
        return new SupplierVi().id(1L).name("name1").contactPerson("contactPerson1").email("email1").phoneNumber("phoneNumber1");
    }

    public static SupplierVi getSupplierViSample2() {
        return new SupplierVi().id(2L).name("name2").contactPerson("contactPerson2").email("email2").phoneNumber("phoneNumber2");
    }

    public static SupplierVi getSupplierViRandomSampleGenerator() {
        return new SupplierVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .contactPerson(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}
