package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class ProductMiMiAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductMiMiAllPropertiesEquals(ProductMiMi expected, ProductMiMi actual) {
        assertProductMiMiAutoGeneratedPropertiesEquals(expected, actual);
        assertProductMiMiAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductMiMiAllUpdatablePropertiesEquals(ProductMiMi expected, ProductMiMi actual) {
        assertProductMiMiUpdatableFieldsEquals(expected, actual);
        assertProductMiMiUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductMiMiAutoGeneratedPropertiesEquals(ProductMiMi expected, ProductMiMi actual) {
        assertThat(expected)
            .as("Verify ProductMiMi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductMiMiUpdatableFieldsEquals(ProductMiMi expected, ProductMiMi actual) {
        assertThat(expected)
            .as("Verify ProductMiMi relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getPrice()).as("check price").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getPrice()))
            .satisfies(e -> assertThat(e.getStock()).as("check stock").isEqualTo(actual.getStock()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductMiMiUpdatableRelationshipsEquals(ProductMiMi expected, ProductMiMi actual) {
        assertThat(expected)
            .as("Verify ProductMiMi relationships")
            .satisfies(e -> assertThat(e.getCategory()).as("check category").isEqualTo(actual.getCategory()))
            .satisfies(e -> assertThat(e.getOrder()).as("check order").isEqualTo(actual.getOrder()))
            .satisfies(e -> assertThat(e.getSuppliers()).as("check suppliers").isEqualTo(actual.getSuppliers()));
    }
}