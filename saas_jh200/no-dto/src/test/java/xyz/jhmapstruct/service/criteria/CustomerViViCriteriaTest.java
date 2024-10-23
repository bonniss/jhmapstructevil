package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerViViCriteriaTest {

    @Test
    void newCustomerViViCriteriaHasAllFiltersNullTest() {
        var customerViViCriteria = new CustomerViViCriteria();
        assertThat(customerViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void customerViViCriteriaFluentMethodsCreatesFiltersTest() {
        var customerViViCriteria = new CustomerViViCriteria();

        setAllFilters(customerViViCriteria);

        assertThat(customerViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void customerViViCriteriaCopyCreatesNullFilterTest() {
        var customerViViCriteria = new CustomerViViCriteria();
        var copy = customerViViCriteria.copy();

        assertThat(customerViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(customerViViCriteria)
        );
    }

    @Test
    void customerViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerViViCriteria = new CustomerViViCriteria();
        setAllFilters(customerViViCriteria);

        var copy = customerViViCriteria.copy();

        assertThat(customerViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(customerViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerViViCriteria = new CustomerViViCriteria();

        assertThat(customerViViCriteria).hasToString("CustomerViViCriteria{}");
    }

    private static void setAllFilters(CustomerViViCriteria customerViViCriteria) {
        customerViViCriteria.id();
        customerViViCriteria.firstName();
        customerViViCriteria.lastName();
        customerViViCriteria.email();
        customerViViCriteria.phoneNumber();
        customerViViCriteria.ordersId();
        customerViViCriteria.tenantId();
        customerViViCriteria.distinct();
    }

    private static Condition<CustomerViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CustomerViViCriteria> copyFiltersAre(
        CustomerViViCriteria copy,
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
