package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerAlphaCriteriaTest {

    @Test
    void newCustomerAlphaCriteriaHasAllFiltersNullTest() {
        var customerAlphaCriteria = new CustomerAlphaCriteria();
        assertThat(customerAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void customerAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var customerAlphaCriteria = new CustomerAlphaCriteria();

        setAllFilters(customerAlphaCriteria);

        assertThat(customerAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void customerAlphaCriteriaCopyCreatesNullFilterTest() {
        var customerAlphaCriteria = new CustomerAlphaCriteria();
        var copy = customerAlphaCriteria.copy();

        assertThat(customerAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(customerAlphaCriteria)
        );
    }

    @Test
    void customerAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerAlphaCriteria = new CustomerAlphaCriteria();
        setAllFilters(customerAlphaCriteria);

        var copy = customerAlphaCriteria.copy();

        assertThat(customerAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(customerAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerAlphaCriteria = new CustomerAlphaCriteria();

        assertThat(customerAlphaCriteria).hasToString("CustomerAlphaCriteria{}");
    }

    private static void setAllFilters(CustomerAlphaCriteria customerAlphaCriteria) {
        customerAlphaCriteria.id();
        customerAlphaCriteria.firstName();
        customerAlphaCriteria.lastName();
        customerAlphaCriteria.email();
        customerAlphaCriteria.phoneNumber();
        customerAlphaCriteria.ordersId();
        customerAlphaCriteria.tenantId();
        customerAlphaCriteria.distinct();
    }

    private static Condition<CustomerAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CustomerAlphaCriteria> copyFiltersAre(
        CustomerAlphaCriteria copy,
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
