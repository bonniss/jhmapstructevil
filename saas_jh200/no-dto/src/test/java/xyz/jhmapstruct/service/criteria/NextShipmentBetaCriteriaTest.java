package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextShipmentBetaCriteriaTest {

    @Test
    void newNextShipmentBetaCriteriaHasAllFiltersNullTest() {
        var nextShipmentBetaCriteria = new NextShipmentBetaCriteria();
        assertThat(nextShipmentBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextShipmentBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextShipmentBetaCriteria = new NextShipmentBetaCriteria();

        setAllFilters(nextShipmentBetaCriteria);

        assertThat(nextShipmentBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextShipmentBetaCriteriaCopyCreatesNullFilterTest() {
        var nextShipmentBetaCriteria = new NextShipmentBetaCriteria();
        var copy = nextShipmentBetaCriteria.copy();

        assertThat(nextShipmentBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentBetaCriteria)
        );
    }

    @Test
    void nextShipmentBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextShipmentBetaCriteria = new NextShipmentBetaCriteria();
        setAllFilters(nextShipmentBetaCriteria);

        var copy = nextShipmentBetaCriteria.copy();

        assertThat(nextShipmentBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextShipmentBetaCriteria = new NextShipmentBetaCriteria();

        assertThat(nextShipmentBetaCriteria).hasToString("NextShipmentBetaCriteria{}");
    }

    private static void setAllFilters(NextShipmentBetaCriteria nextShipmentBetaCriteria) {
        nextShipmentBetaCriteria.id();
        nextShipmentBetaCriteria.trackingNumber();
        nextShipmentBetaCriteria.shippedDate();
        nextShipmentBetaCriteria.deliveryDate();
        nextShipmentBetaCriteria.tenantId();
        nextShipmentBetaCriteria.distinct();
    }

    private static Condition<NextShipmentBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextShipmentBetaCriteria> copyFiltersAre(
        NextShipmentBetaCriteria copy,
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
