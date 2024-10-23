package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerGammaCriteriaTest {

    @Test
    void newCustomerGammaCriteriaHasAllFiltersNullTest() {
        var customerGammaCriteria = new CustomerGammaCriteria();
        assertThat(customerGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void customerGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var customerGammaCriteria = new CustomerGammaCriteria();

        setAllFilters(customerGammaCriteria);

        assertThat(customerGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void customerGammaCriteriaCopyCreatesNullFilterTest() {
        var customerGammaCriteria = new CustomerGammaCriteria();
        var copy = customerGammaCriteria.copy();

        assertThat(customerGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(customerGammaCriteria)
        );
    }

    @Test
    void customerGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerGammaCriteria = new CustomerGammaCriteria();
        setAllFilters(customerGammaCriteria);

        var copy = customerGammaCriteria.copy();

        assertThat(customerGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(customerGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerGammaCriteria = new CustomerGammaCriteria();

        assertThat(customerGammaCriteria).hasToString("CustomerGammaCriteria{}");
    }

    private static void setAllFilters(CustomerGammaCriteria customerGammaCriteria) {
        customerGammaCriteria.id();
        customerGammaCriteria.firstName();
        customerGammaCriteria.lastName();
        customerGammaCriteria.email();
        customerGammaCriteria.phoneNumber();
        customerGammaCriteria.ordersId();
        customerGammaCriteria.tenantId();
        customerGammaCriteria.distinct();
    }

    private static Condition<CustomerGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CustomerGammaCriteria> copyFiltersAre(
        CustomerGammaCriteria copy,
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
