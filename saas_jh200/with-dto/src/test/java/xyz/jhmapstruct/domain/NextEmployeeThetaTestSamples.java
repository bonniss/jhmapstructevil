package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NextEmployeeThetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NextEmployeeTheta getNextEmployeeThetaSample1() {
        return new NextEmployeeTheta().id(1L).firstName("firstName1").lastName("lastName1").email("email1").position("position1");
    }

    public static NextEmployeeTheta getNextEmployeeThetaSample2() {
        return new NextEmployeeTheta().id(2L).firstName("firstName2").lastName("lastName2").email("email2").position("position2");
    }

    public static NextEmployeeTheta getNextEmployeeThetaRandomSampleGenerator() {
        return new NextEmployeeTheta()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .position(UUID.randomUUID().toString());
    }
}
