package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AntonioBanderasTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AntonioBanderas getAntonioBanderasSample1() {
        return new AntonioBanderas()
            .id(1L)
            .level(1)
            .code("code1")
            .name("name1")
            .fullName("fullName1")
            .nativeName("nativeName1")
            .officialCode("officialCode1")
            .divisionTerm("divisionTerm1");
    }

    public static AntonioBanderas getAntonioBanderasSample2() {
        return new AntonioBanderas()
            .id(2L)
            .level(2)
            .code("code2")
            .name("name2")
            .fullName("fullName2")
            .nativeName("nativeName2")
            .officialCode("officialCode2")
            .divisionTerm("divisionTerm2");
    }

    public static AntonioBanderas getAntonioBanderasRandomSampleGenerator() {
        return new AntonioBanderas()
            .id(longCount.incrementAndGet())
            .level(intCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .nativeName(UUID.randomUUID().toString())
            .officialCode(UUID.randomUUID().toString())
            .divisionTerm(UUID.randomUUID().toString());
    }
}
