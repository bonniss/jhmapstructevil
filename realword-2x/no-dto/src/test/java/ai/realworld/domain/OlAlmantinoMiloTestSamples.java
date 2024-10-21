package ai.realworld.domain;

import java.util.UUID;

public class OlAlmantinoMiloTestSamples {

    public static OlAlmantinoMilo getOlAlmantinoMiloSample1() {
        return new OlAlmantinoMilo()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .providerAppManagerId("providerAppManagerId1")
            .name("name1")
            .providerSecretKey("providerSecretKey1")
            .providerToken("providerToken1")
            .providerRefreshToken("providerRefreshToken1");
    }

    public static OlAlmantinoMilo getOlAlmantinoMiloSample2() {
        return new OlAlmantinoMilo()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .providerAppManagerId("providerAppManagerId2")
            .name("name2")
            .providerSecretKey("providerSecretKey2")
            .providerToken("providerToken2")
            .providerRefreshToken("providerRefreshToken2");
    }

    public static OlAlmantinoMilo getOlAlmantinoMiloRandomSampleGenerator() {
        return new OlAlmantinoMilo()
            .id(UUID.randomUUID())
            .providerAppManagerId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .providerSecretKey(UUID.randomUUID().toString())
            .providerToken(UUID.randomUUID().toString())
            .providerRefreshToken(UUID.randomUUID().toString());
    }
}
