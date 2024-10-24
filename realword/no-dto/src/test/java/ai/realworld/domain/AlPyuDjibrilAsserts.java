package ai.realworld.domain;

import static ai.realworld.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class AlPyuDjibrilAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuDjibrilAllPropertiesEquals(AlPyuDjibril expected, AlPyuDjibril actual) {
        assertAlPyuDjibrilAutoGeneratedPropertiesEquals(expected, actual);
        assertAlPyuDjibrilAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuDjibrilAllUpdatablePropertiesEquals(AlPyuDjibril expected, AlPyuDjibril actual) {
        assertAlPyuDjibrilUpdatableFieldsEquals(expected, actual);
        assertAlPyuDjibrilUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuDjibrilAutoGeneratedPropertiesEquals(AlPyuDjibril expected, AlPyuDjibril actual) {
        assertThat(expected)
            .as("Verify AlPyuDjibril auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuDjibrilUpdatableFieldsEquals(AlPyuDjibril expected, AlPyuDjibril actual) {
        assertThat(expected)
            .as("Verify AlPyuDjibril relevant properties")
            .satisfies(e -> assertThat(e.getRateType()).as("check rateType").isEqualTo(actual.getRateType()))
            .satisfies(e -> assertThat(e.getRate()).as("check rate").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getRate()))
            .satisfies(e -> assertThat(e.getIsEnabled()).as("check isEnabled").isEqualTo(actual.getIsEnabled()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuDjibrilUpdatableRelationshipsEquals(AlPyuDjibril expected, AlPyuDjibril actual) {
        assertThat(expected)
            .as("Verify AlPyuDjibril relationships")
            .satisfies(e -> assertThat(e.getProperty()).as("check property").isEqualTo(actual.getProperty()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
