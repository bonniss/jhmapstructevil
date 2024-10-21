package ai.realworld.domain;

import java.util.UUID;

public class JohnLennonTestSamples {

    public static JohnLennon getJohnLennonSample1() {
        return new JohnLennon()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .providerAppId("providerAppId1")
            .name("name1")
            .slug("slug1");
    }

    public static JohnLennon getJohnLennonSample2() {
        return new JohnLennon()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .providerAppId("providerAppId2")
            .name("name2")
            .slug("slug2");
    }

    public static JohnLennon getJohnLennonRandomSampleGenerator() {
        return new JohnLennon()
            .id(UUID.randomUUID())
            .providerAppId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString());
    }
}
