package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AlPacinoTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlPacino getAlPacinoSample1() {
        return new AlPacino()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .platformCode("platformCode1")
            .platformUsername("platformUsername1")
            .platformAvatarUrl("platformAvatarUrl1")
            .familyName("familyName1")
            .givenName("givenName1")
            .phone("phone1")
            .email("email1")
            .currentPoints(1)
            .totalPoints(1);
    }

    public static AlPacino getAlPacinoSample2() {
        return new AlPacino()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .platformCode("platformCode2")
            .platformUsername("platformUsername2")
            .platformAvatarUrl("platformAvatarUrl2")
            .familyName("familyName2")
            .givenName("givenName2")
            .phone("phone2")
            .email("email2")
            .currentPoints(2)
            .totalPoints(2);
    }

    public static AlPacino getAlPacinoRandomSampleGenerator() {
        return new AlPacino()
            .id(UUID.randomUUID())
            .platformCode(UUID.randomUUID().toString())
            .platformUsername(UUID.randomUUID().toString())
            .platformAvatarUrl(UUID.randomUUID().toString())
            .familyName(UUID.randomUUID().toString())
            .givenName(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .currentPoints(intCount.incrementAndGet())
            .totalPoints(intCount.incrementAndGet());
    }
}
