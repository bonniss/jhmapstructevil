package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCustomerSigmaCriteriaTest {

    @Test
    void newNextCustomerSigmaCriteriaHasAllFiltersNullTest() {
        var nextCustomerSigmaCriteria = new NextCustomerSigmaCriteria();
        assertThat(nextCustomerSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCustomerSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCustomerSigmaCriteria = new NextCustomerSigmaCriteria();

        setAllFilters(nextCustomerSigmaCriteria);

        assertThat(nextCustomerSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCustomerSigmaCriteriaCopyCreatesNullFilterTest() {
        var nextCustomerSigmaCriteria = new NextCustomerSigmaCriteria();
        var copy = nextCustomerSigmaCriteria.copy();

        assertThat(nextCustomerSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerSigmaCriteria)
        );
    }

    @Test
    void nextCustomerSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCustomerSigmaCriteria = new NextCustomerSigmaCriteria();
        setAllFilters(nextCustomerSigmaCriteria);

        var copy = nextCustomerSigmaCriteria.copy();

        assertThat(nextCustomerSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCustomerSigmaCriteria = new NextCustomerSigmaCriteria();

        assertThat(nextCustomerSigmaCriteria).hasToString("NextCustomerSigmaCriteria{}");
    }

    private static void setAllFilters(NextCustomerSigmaCriteria nextCustomerSigmaCriteria) {
        nextCustomerSigmaCriteria.id();
        nextCustomerSigmaCriteria.firstName();
        nextCustomerSigmaCriteria.lastName();
        nextCustomerSigmaCriteria.email();
        nextCustomerSigmaCriteria.phoneNumber();
        nextCustomerSigmaCriteria.ordersId();
        nextCustomerSigmaCriteria.tenantId();
        nextCustomerSigmaCriteria.distinct();
    }

    private static Condition<NextCustomerSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCustomerSigmaCriteria> copyFiltersAre(
        NextCustomerSigmaCriteria copy,
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
