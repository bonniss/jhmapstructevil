package ai.realworld.domain;

import java.util.UUID;

public class AlPacinoVoucherViTestSamples {

    public static AlPacinoVoucherVi getAlPacinoVoucherViSample1() {
        return new AlPacinoVoucherVi()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .sourceTitle("sourceTitle1")
            .sourceUrl("sourceUrl1");
    }

    public static AlPacinoVoucherVi getAlPacinoVoucherViSample2() {
        return new AlPacinoVoucherVi()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .sourceTitle("sourceTitle2")
            .sourceUrl("sourceUrl2");
    }

    public static AlPacinoVoucherVi getAlPacinoVoucherViRandomSampleGenerator() {
        return new AlPacinoVoucherVi()
            .id(UUID.randomUUID())
            .sourceTitle(UUID.randomUUID().toString())
            .sourceUrl(UUID.randomUUID().toString());
    }
}
