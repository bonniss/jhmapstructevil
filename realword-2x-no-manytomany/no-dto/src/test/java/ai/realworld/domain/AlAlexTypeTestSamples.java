package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AlAlexTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlAlexType getAlAlexTypeSample1() {
        return new AlAlexType()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .description("description1")
            .configJason("configJason1")
            .treeDepth(1);
    }

    public static AlAlexType getAlAlexTypeSample2() {
        return new AlAlexType()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .description("description2")
            .configJason("configJason2")
            .treeDepth(2);
    }

    public static AlAlexType getAlAlexTypeRandomSampleGenerator() {
        return new AlAlexType()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .configJason(UUID.randomUUID().toString())
            .treeDepth(intCount.incrementAndGet());
    }
}
