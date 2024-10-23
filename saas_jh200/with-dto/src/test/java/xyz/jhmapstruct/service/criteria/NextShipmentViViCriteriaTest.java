package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextShipmentViViCriteriaTest {

    @Test
    void newNextShipmentViViCriteriaHasAllFiltersNullTest() {
        var nextShipmentViViCriteria = new NextShipmentViViCriteria();
        assertThat(nextShipmentViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextShipmentViViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextShipmentViViCriteria = new NextShipmentViViCriteria();

        setAllFilters(nextShipmentViViCriteria);

        assertThat(nextShipmentViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextShipmentViViCriteriaCopyCreatesNullFilterTest() {
        var nextShipmentViViCriteria = new NextShipmentViViCriteria();
        var copy = nextShipmentViViCriteria.copy();

        assertThat(nextShipmentViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentViViCriteria)
        );
    }

    @Test
    void nextShipmentViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextShipmentViViCriteria = new NextShipmentViViCriteria();
        setAllFilters(nextShipmentViViCriteria);

        var copy = nextShipmentViViCriteria.copy();

        assertThat(nextShipmentViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextShipmentViViCriteria = new NextShipmentViViCriteria();

        assertThat(nextShipmentViViCriteria).hasToString("NextShipmentViViCriteria{}");
    }

    private static void setAllFilters(NextShipmentViViCriteria nextShipmentViViCriteria) {
        nextShipmentViViCriteria.id();
        nextShipmentViViCriteria.trackingNumber();
        nextShipmentViViCriteria.shippedDate();
        nextShipmentViViCriteria.deliveryDate();
        nextShipmentViViCriteria.tenantId();
        nextShipmentViViCriteria.distinct();
    }

    private static Condition<NextShipmentViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextShipmentViViCriteria> copyFiltersAre(
        NextShipmentViViCriteria copy,
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
