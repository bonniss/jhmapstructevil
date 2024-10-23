package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCustomerViViCriteriaTest {

    @Test
    void newNextCustomerViViCriteriaHasAllFiltersNullTest() {
        var nextCustomerViViCriteria = new NextCustomerViViCriteria();
        assertThat(nextCustomerViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCustomerViViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCustomerViViCriteria = new NextCustomerViViCriteria();

        setAllFilters(nextCustomerViViCriteria);

        assertThat(nextCustomerViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCustomerViViCriteriaCopyCreatesNullFilterTest() {
        var nextCustomerViViCriteria = new NextCustomerViViCriteria();
        var copy = nextCustomerViViCriteria.copy();

        assertThat(nextCustomerViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerViViCriteria)
        );
    }

    @Test
    void nextCustomerViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCustomerViViCriteria = new NextCustomerViViCriteria();
        setAllFilters(nextCustomerViViCriteria);

        var copy = nextCustomerViViCriteria.copy();

        assertThat(nextCustomerViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCustomerViViCriteria = new NextCustomerViViCriteria();

        assertThat(nextCustomerViViCriteria).hasToString("NextCustomerViViCriteria{}");
    }

    private static void setAllFilters(NextCustomerViViCriteria nextCustomerViViCriteria) {
        nextCustomerViViCriteria.id();
        nextCustomerViViCriteria.firstName();
        nextCustomerViViCriteria.lastName();
        nextCustomerViViCriteria.email();
        nextCustomerViViCriteria.phoneNumber();
        nextCustomerViViCriteria.ordersId();
        nextCustomerViViCriteria.tenantId();
        nextCustomerViViCriteria.distinct();
    }

    private static Condition<NextCustomerViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCustomerViViCriteria> copyFiltersAre(
        NextCustomerViViCriteria copy,
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
