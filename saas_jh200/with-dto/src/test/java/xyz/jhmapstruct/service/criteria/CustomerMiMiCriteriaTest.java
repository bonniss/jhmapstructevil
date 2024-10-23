package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerMiMiCriteriaTest {

    @Test
    void newCustomerMiMiCriteriaHasAllFiltersNullTest() {
        var customerMiMiCriteria = new CustomerMiMiCriteria();
        assertThat(customerMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void customerMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var customerMiMiCriteria = new CustomerMiMiCriteria();

        setAllFilters(customerMiMiCriteria);

        assertThat(customerMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void customerMiMiCriteriaCopyCreatesNullFilterTest() {
        var customerMiMiCriteria = new CustomerMiMiCriteria();
        var copy = customerMiMiCriteria.copy();

        assertThat(customerMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(customerMiMiCriteria)
        );
    }

    @Test
    void customerMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerMiMiCriteria = new CustomerMiMiCriteria();
        setAllFilters(customerMiMiCriteria);

        var copy = customerMiMiCriteria.copy();

        assertThat(customerMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(customerMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerMiMiCriteria = new CustomerMiMiCriteria();

        assertThat(customerMiMiCriteria).hasToString("CustomerMiMiCriteria{}");
    }

    private static void setAllFilters(CustomerMiMiCriteria customerMiMiCriteria) {
        customerMiMiCriteria.id();
        customerMiMiCriteria.firstName();
        customerMiMiCriteria.lastName();
        customerMiMiCriteria.email();
        customerMiMiCriteria.phoneNumber();
        customerMiMiCriteria.ordersId();
        customerMiMiCriteria.tenantId();
        customerMiMiCriteria.distinct();
    }

    private static Condition<CustomerMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CustomerMiMiCriteria> copyFiltersAre(
        CustomerMiMiCriteria copy,
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
