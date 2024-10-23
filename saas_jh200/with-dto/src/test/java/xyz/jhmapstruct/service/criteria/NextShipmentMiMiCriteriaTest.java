package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextShipmentMiMiCriteriaTest {

    @Test
    void newNextShipmentMiMiCriteriaHasAllFiltersNullTest() {
        var nextShipmentMiMiCriteria = new NextShipmentMiMiCriteria();
        assertThat(nextShipmentMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextShipmentMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextShipmentMiMiCriteria = new NextShipmentMiMiCriteria();

        setAllFilters(nextShipmentMiMiCriteria);

        assertThat(nextShipmentMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextShipmentMiMiCriteriaCopyCreatesNullFilterTest() {
        var nextShipmentMiMiCriteria = new NextShipmentMiMiCriteria();
        var copy = nextShipmentMiMiCriteria.copy();

        assertThat(nextShipmentMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentMiMiCriteria)
        );
    }

    @Test
    void nextShipmentMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextShipmentMiMiCriteria = new NextShipmentMiMiCriteria();
        setAllFilters(nextShipmentMiMiCriteria);

        var copy = nextShipmentMiMiCriteria.copy();

        assertThat(nextShipmentMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextShipmentMiMiCriteria = new NextShipmentMiMiCriteria();

        assertThat(nextShipmentMiMiCriteria).hasToString("NextShipmentMiMiCriteria{}");
    }

    private static void setAllFilters(NextShipmentMiMiCriteria nextShipmentMiMiCriteria) {
        nextShipmentMiMiCriteria.id();
        nextShipmentMiMiCriteria.trackingNumber();
        nextShipmentMiMiCriteria.shippedDate();
        nextShipmentMiMiCriteria.deliveryDate();
        nextShipmentMiMiCriteria.tenantId();
        nextShipmentMiMiCriteria.distinct();
    }

    private static Condition<NextShipmentMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextShipmentMiMiCriteria> copyFiltersAre(
        NextShipmentMiMiCriteria copy,
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
