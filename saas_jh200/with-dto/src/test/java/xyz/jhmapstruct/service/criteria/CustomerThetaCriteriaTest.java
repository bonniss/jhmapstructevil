package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerThetaCriteriaTest {

    @Test
    void newCustomerThetaCriteriaHasAllFiltersNullTest() {
        var customerThetaCriteria = new CustomerThetaCriteria();
        assertThat(customerThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void customerThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var customerThetaCriteria = new CustomerThetaCriteria();

        setAllFilters(customerThetaCriteria);

        assertThat(customerThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void customerThetaCriteriaCopyCreatesNullFilterTest() {
        var customerThetaCriteria = new CustomerThetaCriteria();
        var copy = customerThetaCriteria.copy();

        assertThat(customerThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(customerThetaCriteria)
        );
    }

    @Test
    void customerThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerThetaCriteria = new CustomerThetaCriteria();
        setAllFilters(customerThetaCriteria);

        var copy = customerThetaCriteria.copy();

        assertThat(customerThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(customerThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerThetaCriteria = new CustomerThetaCriteria();

        assertThat(customerThetaCriteria).hasToString("CustomerThetaCriteria{}");
    }

    private static void setAllFilters(CustomerThetaCriteria customerThetaCriteria) {
        customerThetaCriteria.id();
        customerThetaCriteria.firstName();
        customerThetaCriteria.lastName();
        customerThetaCriteria.email();
        customerThetaCriteria.phoneNumber();
        customerThetaCriteria.ordersId();
        customerThetaCriteria.tenantId();
        customerThetaCriteria.distinct();
    }

    private static Condition<CustomerThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CustomerThetaCriteria> copyFiltersAre(
        CustomerThetaCriteria copy,
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
