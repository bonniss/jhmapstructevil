package ai.realworld.domain;

import java.util.UUID;

public class OlMasterTestSamples {

    public static OlMaster getOlMasterSample1() {
        return new OlMaster()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .slug("slug1")
            .descriptionHeitiga("descriptionHeitiga1")
            .email("email1")
            .hotline("hotline1")
            .taxCode("taxCode1")
            .contactsJason("contactsJason1")
            .extensionJason("extensionJason1");
    }

    public static OlMaster getOlMasterSample2() {
        return new OlMaster()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .slug("slug2")
            .descriptionHeitiga("descriptionHeitiga2")
            .email("email2")
            .hotline("hotline2")
            .taxCode("taxCode2")
            .contactsJason("contactsJason2")
            .extensionJason("extensionJason2");
    }

    public static OlMaster getOlMasterRandomSampleGenerator() {
        return new OlMaster()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .descriptionHeitiga(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .hotline(UUID.randomUUID().toString())
            .taxCode(UUID.randomUUID().toString())
            .contactsJason(UUID.randomUUID().toString())
            .extensionJason(UUID.randomUUID().toString());
    }
}
