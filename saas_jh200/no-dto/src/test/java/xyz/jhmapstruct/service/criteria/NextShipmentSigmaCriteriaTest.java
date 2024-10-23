package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextShipmentSigmaCriteriaTest {

    @Test
    void newNextShipmentSigmaCriteriaHasAllFiltersNullTest() {
        var nextShipmentSigmaCriteria = new NextShipmentSigmaCriteria();
        assertThat(nextShipmentSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextShipmentSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextShipmentSigmaCriteria = new NextShipmentSigmaCriteria();

        setAllFilters(nextShipmentSigmaCriteria);

        assertThat(nextShipmentSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextShipmentSigmaCriteriaCopyCreatesNullFilterTest() {
        var nextShipmentSigmaCriteria = new NextShipmentSigmaCriteria();
        var copy = nextShipmentSigmaCriteria.copy();

        assertThat(nextShipmentSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentSigmaCriteria)
        );
    }

    @Test
    void nextShipmentSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextShipmentSigmaCriteria = new NextShipmentSigmaCriteria();
        setAllFilters(nextShipmentSigmaCriteria);

        var copy = nextShipmentSigmaCriteria.copy();

        assertThat(nextShipmentSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextShipmentSigmaCriteria = new NextShipmentSigmaCriteria();

        assertThat(nextShipmentSigmaCriteria).hasToString("NextShipmentSigmaCriteria{}");
    }

    private static void setAllFilters(NextShipmentSigmaCriteria nextShipmentSigmaCriteria) {
        nextShipmentSigmaCriteria.id();
        nextShipmentSigmaCriteria.trackingNumber();
        nextShipmentSigmaCriteria.shippedDate();
        nextShipmentSigmaCriteria.deliveryDate();
        nextShipmentSigmaCriteria.tenantId();
        nextShipmentSigmaCriteria.distinct();
    }

    private static Condition<NextShipmentSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextShipmentSigmaCriteria> copyFiltersAre(
        NextShipmentSigmaCriteria copy,
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
