package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerMiCriteriaTest {

    @Test
    void newCustomerMiCriteriaHasAllFiltersNullTest() {
        var customerMiCriteria = new CustomerMiCriteria();
        assertThat(customerMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void customerMiCriteriaFluentMethodsCreatesFiltersTest() {
        var customerMiCriteria = new CustomerMiCriteria();

        setAllFilters(customerMiCriteria);

        assertThat(customerMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void customerMiCriteriaCopyCreatesNullFilterTest() {
        var customerMiCriteria = new CustomerMiCriteria();
        var copy = customerMiCriteria.copy();

        assertThat(customerMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(customerMiCriteria)
        );
    }

    @Test
    void customerMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerMiCriteria = new CustomerMiCriteria();
        setAllFilters(customerMiCriteria);

        var copy = customerMiCriteria.copy();

        assertThat(customerMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(customerMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerMiCriteria = new CustomerMiCriteria();

        assertThat(customerMiCriteria).hasToString("CustomerMiCriteria{}");
    }

    private static void setAllFilters(CustomerMiCriteria customerMiCriteria) {
        customerMiCriteria.id();
        customerMiCriteria.firstName();
        customerMiCriteria.lastName();
        customerMiCriteria.email();
        customerMiCriteria.phoneNumber();
        customerMiCriteria.ordersId();
        customerMiCriteria.tenantId();
        customerMiCriteria.distinct();
    }

    private static Condition<CustomerMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CustomerMiCriteria> copyFiltersAre(CustomerMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
