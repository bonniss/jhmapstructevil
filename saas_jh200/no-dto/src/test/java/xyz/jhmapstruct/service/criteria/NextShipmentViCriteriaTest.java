package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextShipmentViCriteriaTest {

    @Test
    void newNextShipmentViCriteriaHasAllFiltersNullTest() {
        var nextShipmentViCriteria = new NextShipmentViCriteria();
        assertThat(nextShipmentViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextShipmentViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextShipmentViCriteria = new NextShipmentViCriteria();

        setAllFilters(nextShipmentViCriteria);

        assertThat(nextShipmentViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextShipmentViCriteriaCopyCreatesNullFilterTest() {
        var nextShipmentViCriteria = new NextShipmentViCriteria();
        var copy = nextShipmentViCriteria.copy();

        assertThat(nextShipmentViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentViCriteria)
        );
    }

    @Test
    void nextShipmentViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextShipmentViCriteria = new NextShipmentViCriteria();
        setAllFilters(nextShipmentViCriteria);

        var copy = nextShipmentViCriteria.copy();

        assertThat(nextShipmentViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextShipmentViCriteria = new NextShipmentViCriteria();

        assertThat(nextShipmentViCriteria).hasToString("NextShipmentViCriteria{}");
    }

    private static void setAllFilters(NextShipmentViCriteria nextShipmentViCriteria) {
        nextShipmentViCriteria.id();
        nextShipmentViCriteria.trackingNumber();
        nextShipmentViCriteria.shippedDate();
        nextShipmentViCriteria.deliveryDate();
        nextShipmentViCriteria.tenantId();
        nextShipmentViCriteria.distinct();
    }

    private static Condition<NextShipmentViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextShipmentViCriteria> copyFiltersAre(
        NextShipmentViCriteria copy,
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
