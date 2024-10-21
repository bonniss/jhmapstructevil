package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AlDesireTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlDesire getAlDesireSample1() {
        return new AlDesire()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .weight(1)
            .maximumWinningTime(1)
            .awardReference("awardReference1");
    }

    public static AlDesire getAlDesireSample2() {
        return new AlDesire()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .weight(2)
            .maximumWinningTime(2)
            .awardReference("awardReference2");
    }

    public static AlDesire getAlDesireRandomSampleGenerator() {
        return new AlDesire()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .weight(intCount.incrementAndGet())
            .maximumWinningTime(intCount.incrementAndGet())
            .awardReference(UUID.randomUUID().toString());
    }
}
