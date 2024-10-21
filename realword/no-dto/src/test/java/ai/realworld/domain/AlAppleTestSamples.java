package ai.realworld.domain;

import java.util.UUID;

public class AlAppleTestSamples {

    public static AlApple getAlAppleSample1() {
        return new AlApple()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .description("description1")
            .hotline("hotline1")
            .taxCode("taxCode1")
            .contactsJason("contactsJason1")
            .extensionJason("extensionJason1");
    }

    public static AlApple getAlAppleSample2() {
        return new AlApple()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .description("description2")
            .hotline("hotline2")
            .taxCode("taxCode2")
            .contactsJason("contactsJason2")
            .extensionJason("extensionJason2");
    }

    public static AlApple getAlAppleRandomSampleGenerator() {
        return new AlApple()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .hotline(UUID.randomUUID().toString())
            .taxCode(UUID.randomUUID().toString())
            .contactsJason(UUID.randomUUID().toString())
            .extensionJason(UUID.randomUUID().toString());
    }
}
