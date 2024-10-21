package ai.realworld.domain;

import java.util.UUID;

public class AlLadyGagaTestSamples {

    public static AlLadyGaga getAlLadyGagaSample1() {
        return new AlLadyGaga()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .descriptionHeitiga("descriptionHeitiga1");
    }

    public static AlLadyGaga getAlLadyGagaSample2() {
        return new AlLadyGaga()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .descriptionHeitiga("descriptionHeitiga2");
    }

    public static AlLadyGaga getAlLadyGagaRandomSampleGenerator() {
        return new AlLadyGaga().id(UUID.randomUUID()).name(UUID.randomUUID().toString()).descriptionHeitiga(UUID.randomUUID().toString());
    }
}
