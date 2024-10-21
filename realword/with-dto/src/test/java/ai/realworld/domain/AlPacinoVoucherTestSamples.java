package ai.realworld.domain;

import java.util.UUID;

public class AlPacinoVoucherTestSamples {

    public static AlPacinoVoucher getAlPacinoVoucherSample1() {
        return new AlPacinoVoucher()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .sourceTitle("sourceTitle1")
            .sourceUrl("sourceUrl1");
    }

    public static AlPacinoVoucher getAlPacinoVoucherSample2() {
        return new AlPacinoVoucher()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .sourceTitle("sourceTitle2")
            .sourceUrl("sourceUrl2");
    }

    public static AlPacinoVoucher getAlPacinoVoucherRandomSampleGenerator() {
        return new AlPacinoVoucher()
            .id(UUID.randomUUID())
            .sourceTitle(UUID.randomUUID().toString())
            .sourceUrl(UUID.randomUUID().toString());
    }
}
