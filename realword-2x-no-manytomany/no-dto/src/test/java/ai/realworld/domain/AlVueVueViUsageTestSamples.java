package ai.realworld.domain;

import java.util.UUID;

public class AlVueVueViUsageTestSamples {

    public static AlVueVueViUsage getAlVueVueViUsageSample1() {
        return new AlVueVueViUsage().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static AlVueVueViUsage getAlVueVueViUsageSample2() {
        return new AlVueVueViUsage().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static AlVueVueViUsage getAlVueVueViUsageRandomSampleGenerator() {
        return new AlVueVueViUsage().id(UUID.randomUUID());
    }
}
