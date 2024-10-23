package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextEmployeeBetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextEmployeeBeta getNextEmployeeBetaSample1() {
        return new NextEmployeeBeta().id(1L).firstName("firstName1").lastName("lastName1").email("email1").position("position1");
    }

    public static NextEmployeeBeta getNextEmployeeBetaSample2() {
        return new NextEmployeeBeta().id(2L).firstName("firstName2").lastName("lastName2").email("email2").position("position2");
    }

    public static NextEmployeeBeta getNextEmployeeBetaRandomSampleGenerator() {
        return new NextEmployeeBeta()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .position(UUID.randomUUID().toString());
    }
}
