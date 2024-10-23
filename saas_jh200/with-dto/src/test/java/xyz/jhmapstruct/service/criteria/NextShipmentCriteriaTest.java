package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextShipmentCriteriaTest {

    @Test
    void newNextShipmentCriteriaHasAllFiltersNullTest() {
        var nextShipmentCriteria = new NextShipmentCriteria();
        assertThat(nextShipmentCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextShipmentCriteriaFluentMethodsCreatesFiltersTest() {
        var nextShipmentCriteria = new NextShipmentCriteria();

        setAllFilters(nextShipmentCriteria);

        assertThat(nextShipmentCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextShipmentCriteriaCopyCreatesNullFilterTest() {
        var nextShipmentCriteria = new NextShipmentCriteria();
        var copy = nextShipmentCriteria.copy();

        assertThat(nextShipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentCriteria)
        );
    }

    @Test
    void nextShipmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextShipmentCriteria = new NextShipmentCriteria();
        setAllFilters(nextShipmentCriteria);

        var copy = nextShipmentCriteria.copy();

        assertThat(nextShipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextShipmentCriteria = new NextShipmentCriteria();

        assertThat(nextShipmentCriteria).hasToString("NextShipmentCriteria{}");
    }

    private static void setAllFilters(NextShipmentCriteria nextShipmentCriteria) {
        nextShipmentCriteria.id();
        nextShipmentCriteria.trackingNumber();
        nextShipmentCriteria.shippedDate();
        nextShipmentCriteria.deliveryDate();
        nextShipmentCriteria.tenantId();
        nextShipmentCriteria.distinct();
    }

    private static Condition<NextShipmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTrackingNumber()) &&
                condition.apply(criteria.getShippedDate()) &&
                condition.apply(criteria.getDeliveryDate()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NextShipmentCriteria> copyFiltersAre(
        NextShipmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTrackingNumber(), copy.getTrackingNumber()) &&
                condition.apply(criteria.getShippedDate(), copy.getShippedDate()) &&
                condition.apply(criteria.getDeliveryDate(), copy.getDeliveryDate()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
