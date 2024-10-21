package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MetaverseAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMetaverseAllPropertiesEquals(Metaverse expected, Metaverse actual) {
        assertMetaverseAutoGeneratedPropertiesEquals(expected, actual);
        assertMetaverseAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMetaverseAllUpdatablePropertiesEquals(Metaverse expected, Metaverse actual) {
        assertMetaverseUpdatableFieldsEquals(expected, actual);
        assertMetaverseUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMetaverseAutoGeneratedPropertiesEquals(Metaverse expected, Metaverse actual) {
        assertThat(expected)
            .as("Verify Metaverse auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMetaverseUpdatableFieldsEquals(Metaverse expected, Metaverse actual) {
        assertThat(expected)
            .as("Verify Metaverse relevant properties")
            .satisfies(e -> assertThat(e.getFilename()).as("check filename").isEqualTo(actual.getFilename()))
            .satisfies(e -> assertThat(e.getContentType()).as("check contentType").isEqualTo(actual.getContentType()))
            .satisfies(e -> assertThat(e.getFileExt()).as("check fileExt").isEqualTo(actual.getFileExt()))
            .satisfies(e -> assertThat(e.getFileSize()).as("check fileSize").isEqualTo(actual.getFileSize()))
            .satisfies(e -> assertThat(e.getFileUrl()).as("check fileUrl").isEqualTo(actual.getFileUrl()))
            .satisfies(e -> assertThat(e.getThumbnailUrl()).as("check thumbnailUrl").isEqualTo(actual.getThumbnailUrl()))
            .satisfies(e -> assertThat(e.getBlurhash()).as("check blurhash").isEqualTo(actual.getBlurhash()))
            .satisfies(e -> assertThat(e.getObjectName()).as("check objectName").isEqualTo(actual.getObjectName()))
            .satisfies(e -> assertThat(e.getObjectMetaJason()).as("check objectMetaJason").isEqualTo(actual.getObjectMetaJason()))
            .satisfies(e ->
                assertThat(e.getUrlLifespanInSeconds()).as("check urlLifespanInSeconds").isEqualTo(actual.getUrlLifespanInSeconds())
            )
            .satisfies(e -> assertThat(e.getUrlExpiredDate()).as("check urlExpiredDate").isEqualTo(actual.getUrlExpiredDate()))
            .satisfies(e -> assertThat(e.getAutoRenewUrl()).as("check autoRenewUrl").isEqualTo(actual.getAutoRenewUrl()))
            .satisfies(e -> assertThat(e.getIsEnabled()).as("check isEnabled").isEqualTo(actual.getIsEnabled()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMetaverseUpdatableRelationshipsEquals(Metaverse expected, Metaverse actual) {
        assertThat(expected)
            .as("Verify Metaverse relationships")
            .satisfies(e -> assertThat(e.getAlProPros()).as("check alProPros").isEqualTo(actual.getAlProPros()))
            .satisfies(e -> assertThat(e.getAlProties()).as("check alProties").isEqualTo(actual.getAlProties()));
    }
}
