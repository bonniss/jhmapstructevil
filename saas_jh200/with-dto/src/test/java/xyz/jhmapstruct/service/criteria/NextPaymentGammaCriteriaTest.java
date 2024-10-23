package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextPaymentGammaCriteriaTest {

    @Test
    void newNextPaymentGammaCriteriaHasAllFiltersNullTest() {
        var nextPaymentGammaCriteria = new NextPaymentGammaCriteria();
        assertThat(nextPaymentGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextPaymentGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextPaymentGammaCriteria = new NextPaymentGammaCriteria();

        setAllFilters(nextPaymentGammaCriteria);

        assertThat(nextPaymentGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextPaymentGammaCriteriaCopyCreatesNullFilterTest() {
        var nextPaymentGammaCriteria = new NextPaymentGammaCriteria();
        var copy = nextPaymentGammaCriteria.copy();

        assertThat(nextPaymentGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentGammaCriteria)
        );
    }

    @Test
    void nextPaymentGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextPaymentGammaCriteria = new NextPaymentGammaCriteria();
        setAllFilters(nextPaymentGammaCriteria);

        var copy = nextPaymentGammaCriteria.copy();

        assertThat(nextPaymentGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextPaymentGammaCriteria = new NextPaymentGammaCriteria();

        assertThat(nextPaymentGammaCriteria).hasToString("NextPaymentGammaCriteria{}");
    }

    private static void setAllFilters(NextPaymentGammaCriteria nextPaymentGammaCriteria) {
        nextPaymentGammaCriteria.id();
        nextPaymentGammaCriteria.amount();
        nextPaymentGammaCriteria.paymentDate();
        nextPaymentGammaCriteria.paymentMethod();
        nextPaymentGammaCriteria.tenantId();
        nextPaymentGammaCriteria.distinct();
    }

    private static Condition<NextPaymentGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextPaymentGammaCriteria> copyFiltersAre(
        NextPaymentGammaCriteria copy,
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
