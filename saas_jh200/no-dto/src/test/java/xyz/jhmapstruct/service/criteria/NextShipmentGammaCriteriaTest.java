package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextShipmentGammaCriteriaTest {

    @Test
    void newNextShipmentGammaCriteriaHasAllFiltersNullTest() {
        var nextShipmentGammaCriteria = new NextShipmentGammaCriteria();
        assertThat(nextShipmentGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextShipmentGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextShipmentGammaCriteria = new NextShipmentGammaCriteria();

        setAllFilters(nextShipmentGammaCriteria);

        assertThat(nextShipmentGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextShipmentGammaCriteriaCopyCreatesNullFilterTest() {
        var nextShipmentGammaCriteria = new NextShipmentGammaCriteria();
        var copy = nextShipmentGammaCriteria.copy();

        assertThat(nextShipmentGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentGammaCriteria)
        );
    }

    @Test
    void nextShipmentGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextShipmentGammaCriteria = new NextShipmentGammaCriteria();
        setAllFilters(nextShipmentGammaCriteria);

        var copy = nextShipmentGammaCriteria.copy();

        assertThat(nextShipmentGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextShipmentGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextShipmentGammaCriteria = new NextShipmentGammaCriteria();

        assertThat(nextShipmentGammaCriteria).hasToString("NextShipmentGammaCriteria{}");
    }

    private static void setAllFilters(NextShipmentGammaCriteria nextShipmentGammaCriteria) {
        nextShipmentGammaCriteria.id();
        nextShipmentGammaCriteria.trackingNumber();
        nextShipmentGammaCriteria.shippedDate();
        nextShipmentGammaCriteria.deliveryDate();
        nextShipmentGammaCriteria.tenantId();
        nextShipmentGammaCriteria.distinct();
    }

    private static Condition<NextShipmentGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextShipmentGammaCriteria> copyFiltersAre(
        NextShipmentGammaCriteria copy,
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
