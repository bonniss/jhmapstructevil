package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerBetaCriteriaTest {

    @Test
    void newCustomerBetaCriteriaHasAllFiltersNullTest() {
        var customerBetaCriteria = new CustomerBetaCriteria();
        assertThat(customerBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void customerBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var customerBetaCriteria = new CustomerBetaCriteria();

        setAllFilters(customerBetaCriteria);

        assertThat(customerBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void customerBetaCriteriaCopyCreatesNullFilterTest() {
        var customerBetaCriteria = new CustomerBetaCriteria();
        var copy = customerBetaCriteria.copy();

        assertThat(customerBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(customerBetaCriteria)
        );
    }

    @Test
    void customerBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerBetaCriteria = new CustomerBetaCriteria();
        setAllFilters(customerBetaCriteria);

        var copy = customerBetaCriteria.copy();

        assertThat(customerBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(customerBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerBetaCriteria = new CustomerBetaCriteria();

        assertThat(customerBetaCriteria).hasToString("CustomerBetaCriteria{}");
    }

    private static void setAllFilters(CustomerBetaCriteria customerBetaCriteria) {
        customerBetaCriteria.id();
        customerBetaCriteria.firstName();
        customerBetaCriteria.lastName();
        customerBetaCriteria.email();
        customerBetaCriteria.phoneNumber();
        customerBetaCriteria.ordersId();
        customerBetaCriteria.tenantId();
        customerBetaCriteria.distinct();
    }

    private static Condition<CustomerBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CustomerBetaCriteria> copyFiltersAre(
        CustomerBetaCriteria copy,
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
