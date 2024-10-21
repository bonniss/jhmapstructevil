package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AlLeandroTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlLeandro getAlLeandroSample1() {
        return new AlLeandro()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .weight(1)
            .description("description1");
    }

    public static AlLeandro getAlLeandroSample2() {
        return new AlLeandro()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .weight(2)
            .description("description2");
    }

    public static AlLeandro getAlLeandroRandomSampleGenerator() {
        return new AlLeandro()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .weight(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
