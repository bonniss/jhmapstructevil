package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextPaymentViCriteriaTest {

    @Test
    void newNextPaymentViCriteriaHasAllFiltersNullTest() {
        var nextPaymentViCriteria = new NextPaymentViCriteria();
        assertThat(nextPaymentViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextPaymentViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextPaymentViCriteria = new NextPaymentViCriteria();

        setAllFilters(nextPaymentViCriteria);

        assertThat(nextPaymentViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextPaymentViCriteriaCopyCreatesNullFilterTest() {
        var nextPaymentViCriteria = new NextPaymentViCriteria();
        var copy = nextPaymentViCriteria.copy();

        assertThat(nextPaymentViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentViCriteria)
        );
    }

    @Test
    void nextPaymentViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextPaymentViCriteria = new NextPaymentViCriteria();
        setAllFilters(nextPaymentViCriteria);

        var copy = nextPaymentViCriteria.copy();

        assertThat(nextPaymentViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextPaymentViCriteria = new NextPaymentViCriteria();

        assertThat(nextPaymentViCriteria).hasToString("NextPaymentViCriteria{}");
    }

    private static void setAllFilters(NextPaymentViCriteria nextPaymentViCriteria) {
        nextPaymentViCriteria.id();
        nextPaymentViCriteria.amount();
        nextPaymentViCriteria.paymentDate();
        nextPaymentViCriteria.paymentMethod();
        nextPaymentViCriteria.tenantId();
        nextPaymentViCriteria.distinct();
    }

    private static Condition<NextPaymentViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextPaymentViCriteria> copyFiltersAre(
        NextPaymentViCriteria copy,
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
