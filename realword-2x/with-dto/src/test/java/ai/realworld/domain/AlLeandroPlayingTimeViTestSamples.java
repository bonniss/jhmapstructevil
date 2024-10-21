package ai.realworld.domain;

import java.util.UUID;

public class AlLeandroPlayingTimeViTestSamples {

    public static AlLeandroPlayingTimeVi getAlLeandroPlayingTimeViSample1() {
        return new AlLeandroPlayingTimeVi()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .sentAwardToPlayerBy("sentAwardToPlayerBy1")
            .playSourceTime("playSourceTime1");
    }

    public static AlLeandroPlayingTimeVi getAlLeandroPlayingTimeViSample2() {
        return new AlLeandroPlayingTimeVi()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .sentAwardToPlayerBy("sentAwardToPlayerBy2")
            .playSourceTime("playSourceTime2");
    }

    public static AlLeandroPlayingTimeVi getAlLeandroPlayingTimeViRandomSampleGenerator() {
        return new AlLeandroPlayingTimeVi()
            .id(UUID.randomUUID())
            .sentAwardToPlayerBy(UUID.randomUUID().toString())
            .playSourceTime(UUID.randomUUID().toString());
    }
}
