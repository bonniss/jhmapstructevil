package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AlGoogleMeetTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlGoogleMeet getAlGoogleMeetSample1() {
        return new AlGoogleMeet()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .title("title1")
            .description("description1")
            .numberOfParticipants(1)
            .contentJason("contentJason1");
    }

    public static AlGoogleMeet getAlGoogleMeetSample2() {
        return new AlGoogleMeet()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .title("title2")
            .description("description2")
            .numberOfParticipants(2)
            .contentJason("contentJason2");
    }

    public static AlGoogleMeet getAlGoogleMeetRandomSampleGenerator() {
        return new AlGoogleMeet()
            .id(UUID.randomUUID())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .numberOfParticipants(intCount.incrementAndGet())
            .contentJason(UUID.randomUUID().toString());
    }
}
