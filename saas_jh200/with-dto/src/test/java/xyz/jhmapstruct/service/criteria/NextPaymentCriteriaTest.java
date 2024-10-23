package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextPaymentCriteriaTest {

    @Test
    void newNextPaymentCriteriaHasAllFiltersNullTest() {
        var nextPaymentCriteria = new NextPaymentCriteria();
        assertThat(nextPaymentCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextPaymentCriteriaFluentMethodsCreatesFiltersTest() {
        var nextPaymentCriteria = new NextPaymentCriteria();

        setAllFilters(nextPaymentCriteria);

        assertThat(nextPaymentCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextPaymentCriteriaCopyCreatesNullFilterTest() {
        var nextPaymentCriteria = new NextPaymentCriteria();
        var copy = nextPaymentCriteria.copy();

        assertThat(nextPaymentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentCriteria)
        );
    }

    @Test
    void nextPaymentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextPaymentCriteria = new NextPaymentCriteria();
        setAllFilters(nextPaymentCriteria);

        var copy = nextPaymentCriteria.copy();

        assertThat(nextPaymentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextPaymentCriteria = new NextPaymentCriteria();

        assertThat(nextPaymentCriteria).hasToString("NextPaymentCriteria{}");
    }

    private static void setAllFilters(NextPaymentCriteria nextPaymentCriteria) {
        nextPaymentCriteria.id();
        nextPaymentCriteria.amount();
        nextPaymentCriteria.paymentDate();
        nextPaymentCriteria.paymentMethod();
        nextPaymentCriteria.tenantId();
        nextPaymentCriteria.distinct();
    }

    private static Condition<NextPaymentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextPaymentCriteria> copyFiltersAre(NextPaymentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
