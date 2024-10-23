package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextShipmentThetaCriteriaTest {

    @Test
    void newNextShipmentThetaCriteriaHasAllFiltersNullTest() {
        var nextShipmentThetaCriteria = new NextShipmentThetaCriteria();
        assertThat(nextShipmentThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextShipmentThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextShipmentThetaCriteria = new NextShipmentThetaCriteria();

        setAllFilters(nextShipmentThetaCriteria);

        assertThat(nextShipmentThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextShipmentThetaCriteriaCopyCreatesNullFilterTest() {
        var nextShipmentThetaCriteria = new NextShipmentThetaCriteria();
        var copy = nextShipmentThetaCriteria.copy();

        assertThat(nextShipmentThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentThetaCriteria)
        );
    }

    @Test
    void nextShipmentThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextShipmentThetaCriteria = new NextShipmentThetaCriteria();
        setAllFilters(nextShipmentThetaCriteria);

        var copy = nextShipmentThetaCriteria.copy();

        assertThat(nextShipmentThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextShipmentThetaCriteria = new NextShipmentThetaCriteria();

        assertThat(nextShipmentThetaCriteria).hasToString("NextShipmentThetaCriteria{}");
    }

    private static void setAllFilters(NextShipmentThetaCriteria nextShipmentThetaCriteria) {
        nextShipmentThetaCriteria.id();
        nextShipmentThetaCriteria.trackingNumber();
        nextShipmentThetaCriteria.shippedDate();
        nextShipmentThetaCriteria.deliveryDate();
        nextShipmentThetaCriteria.tenantId();
        nextShipmentThetaCriteria.distinct();
    }

    private static Condition<NextShipmentThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextShipmentThetaCriteria> copyFiltersAre(
        NextShipmentThetaCriteria copy,
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