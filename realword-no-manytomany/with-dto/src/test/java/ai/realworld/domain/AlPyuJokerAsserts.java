package ai.realworld.domain;

import static ai.realworld.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class AlPyuJokerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuJokerAllPropertiesEquals(AlPyuJoker expected, AlPyuJoker actual) {
        assertAlPyuJokerAutoGeneratedPropertiesEquals(expected, actual);
        assertAlPyuJokerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuJokerAllUpdatablePropertiesEquals(AlPyuJoker expected, AlPyuJoker actual) {
        assertAlPyuJokerUpdatableFieldsEquals(expected, actual);
        assertAlPyuJokerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuJokerAutoGeneratedPropertiesEquals(AlPyuJoker expected, AlPyuJoker actual) {
        assertThat(expected)
            .as("Verify AlPyuJoker auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuJokerUpdatableFieldsEquals(AlPyuJoker expected, AlPyuJoker actual) {
        assertThat(expected)
            .as("Verify AlPyuJoker relevant properties")
            .satisfies(e -> assertThat(e.getBookingNo()).as("check bookingNo").isEqualTo(actual.getBookingNo()))
            .satisfies(e -> assertThat(e.getNoteHeitiga()).as("check noteHeitiga").isEqualTo(actual.getNoteHeitiga()))
            .satisfies(e -> assertThat(e.getPeriodType()).as("check periodType").isEqualTo(actual.getPeriodType()))
            .satisfies(e -> assertThat(e.getFromDate()).as("check fromDate").isEqualTo(actual.getFromDate()))
            .satisfies(e -> assertThat(e.getToDate()).as("check toDate").isEqualTo(actual.getToDate()))
            .satisfies(e -> assertThat(e.getCheckInDate()).as("check checkInDate").isEqualTo(actual.getCheckInDate()))
            .satisfies(e -> assertThat(e.getCheckOutDate()).as("check checkOutDate").isEqualTo(actual.getCheckOutDate()))
            .satisfies(e -> assertThat(e.getNumberOfAdults()).as("check numberOfAdults").isEqualTo(actual.getNumberOfAdults()))
            .satisfies(e ->
                assertThat(e.getNumberOfPreschoolers()).as("check numberOfPreschoolers").isEqualTo(actual.getNumberOfPreschoolers())
            )
            .satisfies(e -> assertThat(e.getNumberOfChildren()).as("check numberOfChildren").isEqualTo(actual.getNumberOfChildren()))
            .satisfies(e ->
                assertThat(e.getBookingPrice())
                    .as("check bookingPrice")
                    .usingComparator(bigDecimalCompareTo)
                    .isEqualTo(actual.getBookingPrice())
            )
            .satisfies(e ->
                assertThat(e.getExtraFee()).as("check extraFee").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getExtraFee())
            )
            .satisfies(e ->
                assertThat(e.getTotalPrice()).as("check totalPrice").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getTotalPrice())
            )
            .satisfies(e -> assertThat(e.getBookingStatus()).as("check bookingStatus").isEqualTo(actual.getBookingStatus()))
            .satisfies(e -> assertThat(e.getHistoryRefJason()).as("check historyRefJason").isEqualTo(actual.getHistoryRefJason()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPyuJokerUpdatableRelationshipsEquals(AlPyuJoker expected, AlPyuJoker actual) {
        assertThat(expected)
            .as("Verify AlPyuJoker relationships")
            .satisfies(e -> assertThat(e.getCustomer()).as("check customer").isEqualTo(actual.getCustomer()))
            .satisfies(e -> assertThat(e.getPersonInCharge()).as("check personInCharge").isEqualTo(actual.getPersonInCharge()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
