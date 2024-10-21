package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EdSheeranViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EdSheeranVi getEdSheeranViSample1() {
        return new EdSheeranVi()
            .id(1L)
            .familyName("familyName1")
            .givenName("givenName1")
            .display("display1")
            .phone("phone1")
            .contactsJason("contactsJason1");
    }

    public static EdSheeranVi getEdSheeranViSample2() {
        return new EdSheeranVi()
            .id(2L)
            .familyName("familyName2")
            .givenName("givenName2")
            .display("display2")
            .phone("phone2")
            .contactsJason("contactsJason2");
    }

    public static EdSheeranVi getEdSheeranViRandomSampleGenerator() {
        return new EdSheeranVi()
            .id(longCount.incrementAndGet())
            .familyName(UUID.randomUUID().toString())
            .givenName(UUID.randomUUID().toString())
            .display(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .contactsJason(UUID.randomUUID().toString());
    }
}
