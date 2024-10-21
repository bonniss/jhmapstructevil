package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AllMassageThaiAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAllMassageThaiAllPropertiesEquals(AllMassageThai expected, AllMassageThai actual) {
        assertAllMassageThaiAutoGeneratedPropertiesEquals(expected, actual);
        assertAllMassageThaiAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAllMassageThaiAllUpdatablePropertiesEquals(AllMassageThai expected, AllMassageThai actual) {
        assertAllMassageThaiUpdatableFieldsEquals(expected, actual);
        assertAllMassageThaiUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAllMassageThaiAutoGeneratedPropertiesEquals(AllMassageThai expected, AllMassageThai actual) {
        assertThat(expected)
            .as("Verify AllMassageThai auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAllMassageThaiUpdatableFieldsEquals(AllMassageThai expected, AllMassageThai actual) {
        assertThat(expected)
            .as("Verify AllMassageThai relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getTopContent()).as("check topContent").isEqualTo(actual.getTopContent()))
            .satisfies(e -> assertThat(e.getContent()).as("check content").isEqualTo(actual.getContent()))
            .satisfies(e -> assertThat(e.getBottomContent()).as("check bottomContent").isEqualTo(actual.getBottomContent()))
            .satisfies(e ->
                assertThat(e.getPropTitleMappingJason()).as("check propTitleMappingJason").isEqualTo(actual.getPropTitleMappingJason())
            )
            .satisfies(e ->
                assertThat(e.getDataSourceMappingType()).as("check dataSourceMappingType").isEqualTo(actual.getDataSourceMappingType())
            )
            .satisfies(e -> assertThat(e.getTargetUrls()).as("check targetUrls").isEqualTo(actual.getTargetUrls()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAllMassageThaiUpdatableRelationshipsEquals(AllMassageThai expected, AllMassageThai actual) {
        assertThat(expected)
            .as("Verify AllMassageThai relationships")
            .satisfies(e -> assertThat(e.getThumbnail()).as("check thumbnail").isEqualTo(actual.getThumbnail()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}