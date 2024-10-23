package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextShipmentMiCriteriaTest {

    @Test
    void newNextShipmentMiCriteriaHasAllFiltersNullTest() {
        var nextShipmentMiCriteria = new NextShipmentMiCriteria();
        assertThat(nextShipmentMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextShipmentMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextShipmentMiCriteria = new NextShipmentMiCriteria();

        setAllFilters(nextShipmentMiCriteria);

        assertThat(nextShipmentMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextShipmentMiCriteriaCopyCreatesNullFilterTest() {
        var nextShipmentMiCriteria = new NextShipmentMiCriteria();
        var copy = nextShipmentMiCriteria.copy();

        assertThat(nextShipmentMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentMiCriteria)
        );
    }

    @Test
    void nextShipmentMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextShipmentMiCriteria = new NextShipmentMiCriteria();
        setAllFilters(nextShipmentMiCriteria);

        var copy = nextShipmentMiCriteria.copy();

        assertThat(nextShipmentMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextShipmentMiCriteria = new NextShipmentMiCriteria();

        assertThat(nextShipmentMiCriteria).hasToString("NextShipmentMiCriteria{}");
    }

    private static void setAllFilters(NextShipmentMiCriteria nextShipmentMiCriteria) {
        nextShipmentMiCriteria.id();
        nextShipmentMiCriteria.trackingNumber();
        nextShipmentMiCriteria.shippedDate();
        nextShipmentMiCriteria.deliveryDate();
        nextShipmentMiCriteria.tenantId();
        nextShipmentMiCriteria.distinct();
    }

    private static Condition<NextShipmentMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextShipmentMiCriteria> copyFiltersAre(
        NextShipmentMiCriteria copy,
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
