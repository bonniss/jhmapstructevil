package ai.realworld.domain;

import java.util.UUID;

public class AlLeandroPlayingTimeTestSamples {

    public static AlLeandroPlayingTime getAlLeandroPlayingTimeSample1() {
        return new AlLeandroPlayingTime()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .sentAwardToPlayerBy("sentAwardToPlayerBy1")
            .playSourceTime("playSourceTime1");
    }

    public static AlLeandroPlayingTime getAlLeandroPlayingTimeSample2() {
        return new AlLeandroPlayingTime()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .sentAwardToPlayerBy("sentAwardToPlayerBy2")
            .playSourceTime("playSourceTime2");
    }

    public static AlLeandroPlayingTime getAlLeandroPlayingTimeRandomSampleGenerator() {
        return new AlLeandroPlayingTime()
            .id(UUID.randomUUID())
            .sentAwardToPlayerBy(UUID.randomUUID().toString())
            .playSourceTime(UUID.randomUUID().toString());
    }
}
