package ai.realworld.domain;

import java.util.UUID;

public class AlInquiryViTestSamples {

    public static AlInquiryVi getAlInquiryViSample1() {
        return new AlInquiryVi()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .title("title1")
            .body("body1")
            .sender("sender1")
            .email("email1")
            .phone("phone1")
            .contentJason("contentJason1");
    }

    public static AlInquiryVi getAlInquiryViSample2() {
        return new AlInquiryVi()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .title("title2")
            .body("body2")
            .sender("sender2")
            .email("email2")
            .phone("phone2")
            .contentJason("contentJason2");
    }

    public static AlInquiryVi getAlInquiryViRandomSampleGenerator() {
        return new AlInquiryVi()
            .id(UUID.randomUUID())
            .title(UUID.randomUUID().toString())
            .body(UUID.randomUUID().toString())
            .sender(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .contentJason(UUID.randomUUID().toString());
    }
}
