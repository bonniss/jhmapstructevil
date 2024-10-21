package ai.realworld.domain;

import java.util.UUID;

public class AlProtyTestSamples {

    public static AlProty getAlProtySample1() {
        return new AlProty()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .descriptionHeitiga("descriptionHeitiga1")
            .coordinate("coordinate1")
            .code("code1");
    }

    public static AlProty getAlProtySample2() {
        return new AlProty()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .descriptionHeitiga("descriptionHeitiga2")
            .coordinate("coordinate2")
            .code("code2");
    }

    public static AlProty getAlProtyRandomSampleGenerator() {
        return new AlProty()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .descriptionHeitiga(UUID.randomUUID().toString())
            .coordinate(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString());
    }
}
