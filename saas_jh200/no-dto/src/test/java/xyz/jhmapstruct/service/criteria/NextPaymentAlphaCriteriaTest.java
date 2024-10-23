package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextPaymentAlphaCriteriaTest {

    @Test
    void newNextPaymentAlphaCriteriaHasAllFiltersNullTest() {
        var nextPaymentAlphaCriteria = new NextPaymentAlphaCriteria();
        assertThat(nextPaymentAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextPaymentAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextPaymentAlphaCriteria = new NextPaymentAlphaCriteria();

        setAllFilters(nextPaymentAlphaCriteria);

        assertThat(nextPaymentAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextPaymentAlphaCriteriaCopyCreatesNullFilterTest() {
        var nextPaymentAlphaCriteria = new NextPaymentAlphaCriteria();
        var copy = nextPaymentAlphaCriteria.copy();

        assertThat(nextPaymentAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentAlphaCriteria)
        );
    }

    @Test
    void nextPaymentAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextPaymentAlphaCriteria = new NextPaymentAlphaCriteria();
        setAllFilters(nextPaymentAlphaCriteria);

        var copy = nextPaymentAlphaCriteria.copy();

        assertThat(nextPaymentAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextPaymentAlphaCriteria = new NextPaymentAlphaCriteria();

        assertThat(nextPaymentAlphaCriteria).hasToString("NextPaymentAlphaCriteria{}");
    }

    private static void setAllFilters(NextPaymentAlphaCriteria nextPaymentAlphaCriteria) {
        nextPaymentAlphaCriteria.id();
        nextPaymentAlphaCriteria.amount();
        nextPaymentAlphaCriteria.paymentDate();
        nextPaymentAlphaCriteria.paymentMethod();
        nextPaymentAlphaCriteria.tenantId();
        nextPaymentAlphaCriteria.distinct();
    }

    private static Condition<NextPaymentAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextPaymentAlphaCriteria> copyFiltersAre(
        NextPaymentAlphaCriteria copy,
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
