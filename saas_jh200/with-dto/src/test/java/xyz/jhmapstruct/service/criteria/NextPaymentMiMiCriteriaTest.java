package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextPaymentMiMiCriteriaTest {

    @Test
    void newNextPaymentMiMiCriteriaHasAllFiltersNullTest() {
        var nextPaymentMiMiCriteria = new NextPaymentMiMiCriteria();
        assertThat(nextPaymentMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextPaymentMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextPaymentMiMiCriteria = new NextPaymentMiMiCriteria();

        setAllFilters(nextPaymentMiMiCriteria);

        assertThat(nextPaymentMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextPaymentMiMiCriteriaCopyCreatesNullFilterTest() {
        var nextPaymentMiMiCriteria = new NextPaymentMiMiCriteria();
        var copy = nextPaymentMiMiCriteria.copy();

        assertThat(nextPaymentMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentMiMiCriteria)
        );
    }

    @Test
    void nextPaymentMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextPaymentMiMiCriteria = new NextPaymentMiMiCriteria();
        setAllFilters(nextPaymentMiMiCriteria);

        var copy = nextPaymentMiMiCriteria.copy();

        assertThat(nextPaymentMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextPaymentMiMiCriteria = new NextPaymentMiMiCriteria();

        assertThat(nextPaymentMiMiCriteria).hasToString("NextPaymentMiMiCriteria{}");
    }

    private static void setAllFilters(NextPaymentMiMiCriteria nextPaymentMiMiCriteria) {
        nextPaymentMiMiCriteria.id();
        nextPaymentMiMiCriteria.amount();
        nextPaymentMiMiCriteria.paymentDate();
        nextPaymentMiMiCriteria.paymentMethod();
        nextPaymentMiMiCriteria.tenantId();
        nextPaymentMiMiCriteria.distinct();
    }

    private static Condition<NextPaymentMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextPaymentMiMiCriteria> copyFiltersAre(
        NextPaymentMiMiCriteria copy,
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
