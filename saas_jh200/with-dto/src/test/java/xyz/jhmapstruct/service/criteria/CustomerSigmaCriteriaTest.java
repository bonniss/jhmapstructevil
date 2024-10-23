package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerSigmaCriteriaTest {

    @Test
    void newCustomerSigmaCriteriaHasAllFiltersNullTest() {
        var customerSigmaCriteria = new CustomerSigmaCriteria();
        assertThat(customerSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void customerSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var customerSigmaCriteria = new CustomerSigmaCriteria();

        setAllFilters(customerSigmaCriteria);

        assertThat(customerSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void customerSigmaCriteriaCopyCreatesNullFilterTest() {
        var customerSigmaCriteria = new CustomerSigmaCriteria();
        var copy = customerSigmaCriteria.copy();

        assertThat(customerSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(customerSigmaCriteria)
        );
    }

    @Test
    void customerSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerSigmaCriteria = new CustomerSigmaCriteria();
        setAllFilters(customerSigmaCriteria);

        var copy = customerSigmaCriteria.copy();

        assertThat(customerSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(customerSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerSigmaCriteria = new CustomerSigmaCriteria();

        assertThat(customerSigmaCriteria).hasToString("CustomerSigmaCriteria{}");
    }

    private static void setAllFilters(CustomerSigmaCriteria customerSigmaCriteria) {
        customerSigmaCriteria.id();
        customerSigmaCriteria.firstName();
        customerSigmaCriteria.lastName();
        customerSigmaCriteria.email();
        customerSigmaCriteria.phoneNumber();
        customerSigmaCriteria.ordersId();
        customerSigmaCriteria.tenantId();
        customerSigmaCriteria.distinct();
    }

    private static Condition<CustomerSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CustomerSigmaCriteria> copyFiltersAre(
        CustomerSigmaCriteria copy,
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
