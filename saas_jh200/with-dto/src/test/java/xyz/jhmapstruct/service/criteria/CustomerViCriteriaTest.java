package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerViCriteriaTest {

    @Test
    void newCustomerViCriteriaHasAllFiltersNullTest() {
        var customerViCriteria = new CustomerViCriteria();
        assertThat(customerViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void customerViCriteriaFluentMethodsCreatesFiltersTest() {
        var customerViCriteria = new CustomerViCriteria();

        setAllFilters(customerViCriteria);

        assertThat(customerViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void customerViCriteriaCopyCreatesNullFilterTest() {
        var customerViCriteria = new CustomerViCriteria();
        var copy = customerViCriteria.copy();

        assertThat(customerViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(customerViCriteria)
        );
    }

    @Test
    void customerViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerViCriteria = new CustomerViCriteria();
        setAllFilters(customerViCriteria);

        var copy = customerViCriteria.copy();

        assertThat(customerViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(customerViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerViCriteria = new CustomerViCriteria();

        assertThat(customerViCriteria).hasToString("CustomerViCriteria{}");
    }

    private static void setAllFilters(CustomerViCriteria customerViCriteria) {
        customerViCriteria.id();
        customerViCriteria.firstName();
        customerViCriteria.lastName();
        customerViCriteria.email();
        customerViCriteria.phoneNumber();
        customerViCriteria.ordersId();
        customerViCriteria.tenantId();
        customerViCriteria.distinct();
    }

    private static Condition<CustomerViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CustomerViCriteria> copyFiltersAre(CustomerViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
