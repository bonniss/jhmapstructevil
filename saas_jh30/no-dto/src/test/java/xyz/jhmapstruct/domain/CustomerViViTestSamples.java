package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerViViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CustomerViVi getCustomerViViSample1() {
        return new CustomerViVi().id(1L).firstName("firstName1").lastName("lastName1").email("email1").phoneNumber("phoneNumber1");
    }

    public static CustomerViVi getCustomerViViSample2() {
        return new CustomerViVi().id(2L).firstName("firstName2").lastName("lastName2").email("email2").phoneNumber("phoneNumber2");
    }

    public static CustomerViVi getCustomerViViRandomSampleGenerator() {
        return new CustomerViVi()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}
