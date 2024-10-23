package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCustomerGammaCriteriaTest {

    @Test
    void newNextCustomerGammaCriteriaHasAllFiltersNullTest() {
        var nextCustomerGammaCriteria = new NextCustomerGammaCriteria();
        assertThat(nextCustomerGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCustomerGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCustomerGammaCriteria = new NextCustomerGammaCriteria();

        setAllFilters(nextCustomerGammaCriteria);

        assertThat(nextCustomerGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCustomerGammaCriteriaCopyCreatesNullFilterTest() {
        var nextCustomerGammaCriteria = new NextCustomerGammaCriteria();
        var copy = nextCustomerGammaCriteria.copy();

        assertThat(nextCustomerGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerGammaCriteria)
        );
    }

    @Test
    void nextCustomerGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCustomerGammaCriteria = new NextCustomerGammaCriteria();
        setAllFilters(nextCustomerGammaCriteria);

        var copy = nextCustomerGammaCriteria.copy();

        assertThat(nextCustomerGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCustomerGammaCriteria = new NextCustomerGammaCriteria();

        assertThat(nextCustomerGammaCriteria).hasToString("NextCustomerGammaCriteria{}");
    }

    private static void setAllFilters(NextCustomerGammaCriteria nextCustomerGammaCriteria) {
        nextCustomerGammaCriteria.id();
        nextCustomerGammaCriteria.firstName();
        nextCustomerGammaCriteria.lastName();
        nextCustomerGammaCriteria.email();
        nextCustomerGammaCriteria.phoneNumber();
        nextCustomerGammaCriteria.ordersId();
        nextCustomerGammaCriteria.tenantId();
        nextCustomerGammaCriteria.distinct();
    }

    private static Condition<NextCustomerGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCustomerGammaCriteria> copyFiltersAre(
        NextCustomerGammaCriteria copy,
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
