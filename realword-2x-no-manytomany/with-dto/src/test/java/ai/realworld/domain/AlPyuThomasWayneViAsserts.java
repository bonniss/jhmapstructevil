package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlPyuThomasWayneViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuThomasWayneViAllPropertiesEquals(AlPyuThomasWayneVi expected, AlPyuThomasWayneVi actual) {
        assertAlPyuThomasWayneViAutoGeneratedPropertiesEquals(expected, actual);
        assertAlPyuThomasWayneViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuThomasWayneViAllUpdatablePropertiesEquals(AlPyuThomasWayneVi expected, AlPyuThomasWayneVi actual) {
        assertAlPyuThomasWayneViUpdatableFieldsEquals(expected, actual);
        assertAlPyuThomasWayneViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuThomasWayneViAutoGeneratedPropertiesEquals(AlPyuThomasWayneVi expected, AlPyuThomasWayneVi actual) {
        assertThat(expected)
            .as("Verify AlPyuThomasWayneVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuThomasWayneViUpdatableFieldsEquals(AlPyuThomasWayneVi expected, AlPyuThomasWayneVi actual) {
        assertThat(expected)
            .as("Verify AlPyuThomasWayneVi relevant properties")
            .satisfies(e -> assertThat(e.getRating()).as("check rating").isEqualTo(actual.getRating()))
            .satisfies(e -> assertThat(e.getComment()).as("check comment").isEqualTo(actual.getComment()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuThomasWayneViUpdatableRelationshipsEquals(AlPyuThomasWayneVi expected, AlPyuThomasWayneVi actual) {
        assertThat(expected)
            .as("Verify AlPyuThomasWayneVi relationships")
            .satisfies(e -> assertThat(e.getBooking()).as("check booking").isEqualTo(actual.getBooking()));
    }
}
