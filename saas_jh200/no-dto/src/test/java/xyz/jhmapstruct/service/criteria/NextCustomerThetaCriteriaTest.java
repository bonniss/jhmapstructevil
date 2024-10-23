package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCustomerThetaCriteriaTest {

    @Test
    void newNextCustomerThetaCriteriaHasAllFiltersNullTest() {
        var nextCustomerThetaCriteria = new NextCustomerThetaCriteria();
        assertThat(nextCustomerThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCustomerThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCustomerThetaCriteria = new NextCustomerThetaCriteria();

        setAllFilters(nextCustomerThetaCriteria);

        assertThat(nextCustomerThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCustomerThetaCriteriaCopyCreatesNullFilterTest() {
        var nextCustomerThetaCriteria = new NextCustomerThetaCriteria();
        var copy = nextCustomerThetaCriteria.copy();

        assertThat(nextCustomerThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerThetaCriteria)
        );
    }

    @Test
    void nextCustomerThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCustomerThetaCriteria = new NextCustomerThetaCriteria();
        setAllFilters(nextCustomerThetaCriteria);

        var copy = nextCustomerThetaCriteria.copy();

        assertThat(nextCustomerThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCustomerThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCustomerThetaCriteria = new NextCustomerThetaCriteria();

        assertThat(nextCustomerThetaCriteria).hasToString("NextCustomerThetaCriteria{}");
    }

    private static void setAllFilters(NextCustomerThetaCriteria nextCustomerThetaCriteria) {
        nextCustomerThetaCriteria.id();
        nextCustomerThetaCriteria.firstName();
        nextCustomerThetaCriteria.lastName();
        nextCustomerThetaCriteria.email();
        nextCustomerThetaCriteria.phoneNumber();
        nextCustomerThetaCriteria.ordersId();
        nextCustomerThetaCriteria.tenantId();
        nextCustomerThetaCriteria.distinct();
    }

    private static Condition<NextCustomerThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCustomerThetaCriteria> copyFiltersAre(
        NextCustomerThetaCriteria copy,
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
