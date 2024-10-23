package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCustomerAlphaCriteriaTest {

    @Test
    void newNextCustomerAlphaCriteriaHasAllFiltersNullTest() {
        var nextCustomerAlphaCriteria = new NextCustomerAlphaCriteria();
        assertThat(nextCustomerAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCustomerAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCustomerAlphaCriteria = new NextCustomerAlphaCriteria();

        setAllFilters(nextCustomerAlphaCriteria);

        assertThat(nextCustomerAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCustomerAlphaCriteriaCopyCreatesNullFilterTest() {
        var nextCustomerAlphaCriteria = new NextCustomerAlphaCriteria();
        var copy = nextCustomerAlphaCriteria.copy();

        assertThat(nextCustomerAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerAlphaCriteria)
        );
    }

    @Test
    void nextCustomerAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCustomerAlphaCriteria = new NextCustomerAlphaCriteria();
        setAllFilters(nextCustomerAlphaCriteria);

        var copy = nextCustomerAlphaCriteria.copy();

        assertThat(nextCustomerAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCustomerAlphaCriteria = new NextCustomerAlphaCriteria();

        assertThat(nextCustomerAlphaCriteria).hasToString("NextCustomerAlphaCriteria{}");
    }

    private static void setAllFilters(NextCustomerAlphaCriteria nextCustomerAlphaCriteria) {
        nextCustomerAlphaCriteria.id();
        nextCustomerAlphaCriteria.firstName();
        nextCustomerAlphaCriteria.lastName();
        nextCustomerAlphaCriteria.email();
        nextCustomerAlphaCriteria.phoneNumber();
        nextCustomerAlphaCriteria.ordersId();
        nextCustomerAlphaCriteria.tenantId();
        nextCustomerAlphaCriteria.distinct();
    }

    private static Condition<NextCustomerAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCustomerAlphaCriteria> copyFiltersAre(
        NextCustomerAlphaCriteria copy,
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
