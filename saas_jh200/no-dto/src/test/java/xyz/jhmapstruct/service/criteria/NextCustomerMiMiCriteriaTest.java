package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCustomerMiMiCriteriaTest {

    @Test
    void newNextCustomerMiMiCriteriaHasAllFiltersNullTest() {
        var nextCustomerMiMiCriteria = new NextCustomerMiMiCriteria();
        assertThat(nextCustomerMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCustomerMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCustomerMiMiCriteria = new NextCustomerMiMiCriteria();

        setAllFilters(nextCustomerMiMiCriteria);

        assertThat(nextCustomerMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCustomerMiMiCriteriaCopyCreatesNullFilterTest() {
        var nextCustomerMiMiCriteria = new NextCustomerMiMiCriteria();
        var copy = nextCustomerMiMiCriteria.copy();

        assertThat(nextCustomerMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerMiMiCriteria)
        );
    }

    @Test
    void nextCustomerMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCustomerMiMiCriteria = new NextCustomerMiMiCriteria();
        setAllFilters(nextCustomerMiMiCriteria);

        var copy = nextCustomerMiMiCriteria.copy();

        assertThat(nextCustomerMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCustomerMiMiCriteria = new NextCustomerMiMiCriteria();

        assertThat(nextCustomerMiMiCriteria).hasToString("NextCustomerMiMiCriteria{}");
    }

    private static void setAllFilters(NextCustomerMiMiCriteria nextCustomerMiMiCriteria) {
        nextCustomerMiMiCriteria.id();
        nextCustomerMiMiCriteria.firstName();
        nextCustomerMiMiCriteria.lastName();
        nextCustomerMiMiCriteria.email();
        nextCustomerMiMiCriteria.phoneNumber();
        nextCustomerMiMiCriteria.ordersId();
        nextCustomerMiMiCriteria.tenantId();
        nextCustomerMiMiCriteria.distinct();
    }

    private static Condition<NextCustomerMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCustomerMiMiCriteria> copyFiltersAre(
        NextCustomerMiMiCriteria copy,
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
