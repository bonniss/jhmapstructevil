package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextPaymentMiCriteriaTest {

    @Test
    void newNextPaymentMiCriteriaHasAllFiltersNullTest() {
        var nextPaymentMiCriteria = new NextPaymentMiCriteria();
        assertThat(nextPaymentMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextPaymentMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextPaymentMiCriteria = new NextPaymentMiCriteria();

        setAllFilters(nextPaymentMiCriteria);

        assertThat(nextPaymentMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextPaymentMiCriteriaCopyCreatesNullFilterTest() {
        var nextPaymentMiCriteria = new NextPaymentMiCriteria();
        var copy = nextPaymentMiCriteria.copy();

        assertThat(nextPaymentMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentMiCriteria)
        );
    }

    @Test
    void nextPaymentMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextPaymentMiCriteria = new NextPaymentMiCriteria();
        setAllFilters(nextPaymentMiCriteria);

        var copy = nextPaymentMiCriteria.copy();

        assertThat(nextPaymentMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextPaymentMiCriteria = new NextPaymentMiCriteria();

        assertThat(nextPaymentMiCriteria).hasToString("NextPaymentMiCriteria{}");
    }

    private static void setAllFilters(NextPaymentMiCriteria nextPaymentMiCriteria) {
        nextPaymentMiCriteria.id();
        nextPaymentMiCriteria.amount();
        nextPaymentMiCriteria.paymentDate();
        nextPaymentMiCriteria.paymentMethod();
        nextPaymentMiCriteria.tenantId();
        nextPaymentMiCriteria.distinct();
    }

    private static Condition<NextPaymentMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextPaymentMiCriteria> copyFiltersAre(
        NextPaymentMiCriteria copy,
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
