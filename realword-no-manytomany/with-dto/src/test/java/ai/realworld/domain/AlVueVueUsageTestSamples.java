package ai.realworld.domain;

import java.util.UUID;

public class AlVueVueUsageTestSamples {

    public static AlVueVueUsage getAlVueVueUsageSample1() {
        return new AlVueVueUsage().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static AlVueVueUsage getAlVueVueUsageSample2() {
        return new AlVueVueUsage().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static AlVueVueUsage getAlVueVueUsageRandomSampleGenerator() {
        return new AlVueVueUsage().id(UUID.randomUUID());
    }
}
