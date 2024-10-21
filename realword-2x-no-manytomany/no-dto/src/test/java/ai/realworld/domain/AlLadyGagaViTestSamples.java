package ai.realworld.domain;

import java.util.UUID;

public class AlLadyGagaViTestSamples {

    public static AlLadyGagaVi getAlLadyGagaViSample1() {
        return new AlLadyGagaVi()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .descriptionHeitiga("descriptionHeitiga1");
    }

    public static AlLadyGagaVi getAlLadyGagaViSample2() {
        return new AlLadyGagaVi()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .descriptionHeitiga("descriptionHeitiga2");
    }

    public static AlLadyGagaVi getAlLadyGagaViRandomSampleGenerator() {
        return new AlLadyGagaVi().id(UUID.randomUUID()).name(UUID.randomUUID().toString()).descriptionHeitiga(UUID.randomUUID().toString());
    }
}
