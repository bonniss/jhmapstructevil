package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextPaymentThetaCriteriaTest {

    @Test
    void newNextPaymentThetaCriteriaHasAllFiltersNullTest() {
        var nextPaymentThetaCriteria = new NextPaymentThetaCriteria();
        assertThat(nextPaymentThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextPaymentThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextPaymentThetaCriteria = new NextPaymentThetaCriteria();

        setAllFilters(nextPaymentThetaCriteria);

        assertThat(nextPaymentThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextPaymentThetaCriteriaCopyCreatesNullFilterTest() {
        var nextPaymentThetaCriteria = new NextPaymentThetaCriteria();
        var copy = nextPaymentThetaCriteria.copy();

        assertThat(nextPaymentThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentThetaCriteria)
        );
    }

    @Test
    void nextPaymentThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextPaymentThetaCriteria = new NextPaymentThetaCriteria();
        setAllFilters(nextPaymentThetaCriteria);

        var copy = nextPaymentThetaCriteria.copy();

        assertThat(nextPaymentThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextPaymentThetaCriteria = new NextPaymentThetaCriteria();

        assertThat(nextPaymentThetaCriteria).hasToString("NextPaymentThetaCriteria{}");
    }

    private static void setAllFilters(NextPaymentThetaCriteria nextPaymentThetaCriteria) {
        nextPaymentThetaCriteria.id();
        nextPaymentThetaCriteria.amount();
        nextPaymentThetaCriteria.paymentDate();
        nextPaymentThetaCriteria.paymentMethod();
        nextPaymentThetaCriteria.tenantId();
        nextPaymentThetaCriteria.distinct();
    }

    private static Condition<NextPaymentThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextPaymentThetaCriteria> copyFiltersAre(
        NextPaymentThetaCriteria copy,
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
