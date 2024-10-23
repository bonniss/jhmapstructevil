package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCustomerCriteriaTest {

    @Test
    void newNextCustomerCriteriaHasAllFiltersNullTest() {
        var nextCustomerCriteria = new NextCustomerCriteria();
        assertThat(nextCustomerCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCustomerCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCustomerCriteria = new NextCustomerCriteria();

        setAllFilters(nextCustomerCriteria);

        assertThat(nextCustomerCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCustomerCriteriaCopyCreatesNullFilterTest() {
        var nextCustomerCriteria = new NextCustomerCriteria();
        var copy = nextCustomerCriteria.copy();

        assertThat(nextCustomerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerCriteria)
        );
    }

    @Test
    void nextCustomerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCustomerCriteria = new NextCustomerCriteria();
        setAllFilters(nextCustomerCriteria);

        var copy = nextCustomerCriteria.copy();

        assertThat(nextCustomerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCustomerCriteria = new NextCustomerCriteria();

        assertThat(nextCustomerCriteria).hasToString("NextCustomerCriteria{}");
    }

    private static void setAllFilters(NextCustomerCriteria nextCustomerCriteria) {
        nextCustomerCriteria.id();
        nextCustomerCriteria.firstName();
        nextCustomerCriteria.lastName();
        nextCustomerCriteria.email();
        nextCustomerCriteria.phoneNumber();
        nextCustomerCriteria.ordersId();
        nextCustomerCriteria.tenantId();
        nextCustomerCriteria.distinct();
    }

    private static Condition<NextCustomerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCustomerCriteria> copyFiltersAre(
        NextCustomerCriteria copy,
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
