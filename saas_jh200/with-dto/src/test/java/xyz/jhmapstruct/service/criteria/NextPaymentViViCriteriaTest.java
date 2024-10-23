package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextPaymentViViCriteriaTest {

    @Test
    void newNextPaymentViViCriteriaHasAllFiltersNullTest() {
        var nextPaymentViViCriteria = new NextPaymentViViCriteria();
        assertThat(nextPaymentViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextPaymentViViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextPaymentViViCriteria = new NextPaymentViViCriteria();

        setAllFilters(nextPaymentViViCriteria);

        assertThat(nextPaymentViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextPaymentViViCriteriaCopyCreatesNullFilterTest() {
        var nextPaymentViViCriteria = new NextPaymentViViCriteria();
        var copy = nextPaymentViViCriteria.copy();

        assertThat(nextPaymentViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentViViCriteria)
        );
    }

    @Test
    void nextPaymentViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextPaymentViViCriteria = new NextPaymentViViCriteria();
        setAllFilters(nextPaymentViViCriteria);

        var copy = nextPaymentViViCriteria.copy();

        assertThat(nextPaymentViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextPaymentViViCriteria = new NextPaymentViViCriteria();

        assertThat(nextPaymentViViCriteria).hasToString("NextPaymentViViCriteria{}");
    }

    private static void setAllFilters(NextPaymentViViCriteria nextPaymentViViCriteria) {
        nextPaymentViViCriteria.id();
        nextPaymentViViCriteria.amount();
        nextPaymentViViCriteria.paymentDate();
        nextPaymentViViCriteria.paymentMethod();
        nextPaymentViViCriteria.tenantId();
        nextPaymentViViCriteria.distinct();
    }

    private static Condition<NextPaymentViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextPaymentViViCriteria> copyFiltersAre(
        NextPaymentViViCriteria copy,
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
