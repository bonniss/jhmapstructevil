package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextPaymentBetaCriteriaTest {

    @Test
    void newNextPaymentBetaCriteriaHasAllFiltersNullTest() {
        var nextPaymentBetaCriteria = new NextPaymentBetaCriteria();
        assertThat(nextPaymentBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextPaymentBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextPaymentBetaCriteria = new NextPaymentBetaCriteria();

        setAllFilters(nextPaymentBetaCriteria);

        assertThat(nextPaymentBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextPaymentBetaCriteriaCopyCreatesNullFilterTest() {
        var nextPaymentBetaCriteria = new NextPaymentBetaCriteria();
        var copy = nextPaymentBetaCriteria.copy();

        assertThat(nextPaymentBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentBetaCriteria)
        );
    }

    @Test
    void nextPaymentBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextPaymentBetaCriteria = new NextPaymentBetaCriteria();
        setAllFilters(nextPaymentBetaCriteria);

        var copy = nextPaymentBetaCriteria.copy();

        assertThat(nextPaymentBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextPaymentBetaCriteria = new NextPaymentBetaCriteria();

        assertThat(nextPaymentBetaCriteria).hasToString("NextPaymentBetaCriteria{}");
    }

    private static void setAllFilters(NextPaymentBetaCriteria nextPaymentBetaCriteria) {
        nextPaymentBetaCriteria.id();
        nextPaymentBetaCriteria.amount();
        nextPaymentBetaCriteria.paymentDate();
        nextPaymentBetaCriteria.paymentMethod();
        nextPaymentBetaCriteria.tenantId();
        nextPaymentBetaCriteria.distinct();
    }

    private static Condition<NextPaymentBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getPaymentDate()) &&
                condition.apply(criteria.getPaymentMethod()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NextPaymentBetaCriteria> copyFiltersAre(
        NextPaymentBetaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getPaymentDate(), copy.getPaymentDate()) &&
                condition.apply(criteria.getPaymentMethod(), copy.getPaymentMethod()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
