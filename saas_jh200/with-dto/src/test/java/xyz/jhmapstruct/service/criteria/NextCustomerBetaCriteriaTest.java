package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCustomerBetaCriteriaTest {

    @Test
    void newNextCustomerBetaCriteriaHasAllFiltersNullTest() {
        var nextCustomerBetaCriteria = new NextCustomerBetaCriteria();
        assertThat(nextCustomerBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCustomerBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCustomerBetaCriteria = new NextCustomerBetaCriteria();

        setAllFilters(nextCustomerBetaCriteria);

        assertThat(nextCustomerBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCustomerBetaCriteriaCopyCreatesNullFilterTest() {
        var nextCustomerBetaCriteria = new NextCustomerBetaCriteria();
        var copy = nextCustomerBetaCriteria.copy();

        assertThat(nextCustomerBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerBetaCriteria)
        );
    }

    @Test
    void nextCustomerBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCustomerBetaCriteria = new NextCustomerBetaCriteria();
        setAllFilters(nextCustomerBetaCriteria);

        var copy = nextCustomerBetaCriteria.copy();

        assertThat(nextCustomerBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCustomerBetaCriteria = new NextCustomerBetaCriteria();

        assertThat(nextCustomerBetaCriteria).hasToString("NextCustomerBetaCriteria{}");
    }

    private static void setAllFilters(NextCustomerBetaCriteria nextCustomerBetaCriteria) {
        nextCustomerBetaCriteria.id();
        nextCustomerBetaCriteria.firstName();
        nextCustomerBetaCriteria.lastName();
        nextCustomerBetaCriteria.email();
        nextCustomerBetaCriteria.phoneNumber();
        nextCustomerBetaCriteria.ordersId();
        nextCustomerBetaCriteria.tenantId();
        nextCustomerBetaCriteria.distinct();
    }

    private static Condition<NextCustomerBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getOrdersId()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NextCustomerBetaCriteria> copyFiltersAre(
        NextCustomerBetaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getOrdersId(), copy.getOrdersId()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
