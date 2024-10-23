package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCustomerViCriteriaTest {

    @Test
    void newNextCustomerViCriteriaHasAllFiltersNullTest() {
        var nextCustomerViCriteria = new NextCustomerViCriteria();
        assertThat(nextCustomerViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCustomerViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCustomerViCriteria = new NextCustomerViCriteria();

        setAllFilters(nextCustomerViCriteria);

        assertThat(nextCustomerViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCustomerViCriteriaCopyCreatesNullFilterTest() {
        var nextCustomerViCriteria = new NextCustomerViCriteria();
        var copy = nextCustomerViCriteria.copy();

        assertThat(nextCustomerViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerViCriteria)
        );
    }

    @Test
    void nextCustomerViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCustomerViCriteria = new NextCustomerViCriteria();
        setAllFilters(nextCustomerViCriteria);

        var copy = nextCustomerViCriteria.copy();

        assertThat(nextCustomerViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCustomerViCriteria = new NextCustomerViCriteria();

        assertThat(nextCustomerViCriteria).hasToString("NextCustomerViCriteria{}");
    }

    private static void setAllFilters(NextCustomerViCriteria nextCustomerViCriteria) {
        nextCustomerViCriteria.id();
        nextCustomerViCriteria.firstName();
        nextCustomerViCriteria.lastName();
        nextCustomerViCriteria.email();
        nextCustomerViCriteria.phoneNumber();
        nextCustomerViCriteria.ordersId();
        nextCustomerViCriteria.tenantId();
        nextCustomerViCriteria.distinct();
    }

    private static Condition<NextCustomerViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCustomerViCriteria> copyFiltersAre(
        NextCustomerViCriteria copy,
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
