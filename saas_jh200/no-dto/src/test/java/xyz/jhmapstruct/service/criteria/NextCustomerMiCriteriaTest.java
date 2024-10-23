package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCustomerMiCriteriaTest {

    @Test
    void newNextCustomerMiCriteriaHasAllFiltersNullTest() {
        var nextCustomerMiCriteria = new NextCustomerMiCriteria();
        assertThat(nextCustomerMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCustomerMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCustomerMiCriteria = new NextCustomerMiCriteria();

        setAllFilters(nextCustomerMiCriteria);

        assertThat(nextCustomerMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCustomerMiCriteriaCopyCreatesNullFilterTest() {
        var nextCustomerMiCriteria = new NextCustomerMiCriteria();
        var copy = nextCustomerMiCriteria.copy();

        assertThat(nextCustomerMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerMiCriteria)
        );
    }

    @Test
    void nextCustomerMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCustomerMiCriteria = new NextCustomerMiCriteria();
        setAllFilters(nextCustomerMiCriteria);

        var copy = nextCustomerMiCriteria.copy();

        assertThat(nextCustomerMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCustomerMiCriteria = new NextCustomerMiCriteria();

        assertThat(nextCustomerMiCriteria).hasToString("NextCustomerMiCriteria{}");
    }

    private static void setAllFilters(NextCustomerMiCriteria nextCustomerMiCriteria) {
        nextCustomerMiCriteria.id();
        nextCustomerMiCriteria.firstName();
        nextCustomerMiCriteria.lastName();
        nextCustomerMiCriteria.email();
        nextCustomerMiCriteria.phoneNumber();
        nextCustomerMiCriteria.ordersId();
        nextCustomerMiCriteria.tenantId();
        nextCustomerMiCriteria.distinct();
    }

    private static Condition<NextCustomerMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCustomerMiCriteria> copyFiltersAre(
        NextCustomerMiCriteria copy,
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
