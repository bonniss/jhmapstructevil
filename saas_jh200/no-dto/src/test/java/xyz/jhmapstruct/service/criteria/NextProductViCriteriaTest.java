package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextProductViCriteriaTest {

    @Test
    void newNextProductViCriteriaHasAllFiltersNullTest() {
        var nextProductViCriteria = new NextProductViCriteria();
        assertThat(nextProductViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextProductViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextProductViCriteria = new NextProductViCriteria();

        setAllFilters(nextProductViCriteria);

        assertThat(nextProductViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextProductViCriteriaCopyCreatesNullFilterTest() {
        var nextProductViCriteria = new NextProductViCriteria();
        var copy = nextProductViCriteria.copy();

        assertThat(nextProductViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductViCriteria)
        );
    }

    @Test
    void nextProductViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextProductViCriteria = new NextProductViCriteria();
        setAllFilters(nextProductViCriteria);

        var copy = nextProductViCriteria.copy();

        assertThat(nextProductViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextProductViCriteria = new NextProductViCriteria();

        assertThat(nextProductViCriteria).hasToString("NextProductViCriteria{}");
    }

    private static void setAllFilters(NextProductViCriteria nextProductViCriteria) {
        nextProductViCriteria.id();
        nextProductViCriteria.name();
        nextProductViCriteria.price();
        nextProductViCriteria.stock();
        nextProductViCriteria.categoryId();
        nextProductViCriteria.tenantId();
        nextProductViCriteria.orderId();
        nextProductViCriteria.suppliersId();
        nextProductViCriteria.distinct();
    }

    private static Condition<NextProductViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getStock()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getSuppliersId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NextProductViCriteria> copyFiltersAre(
        NextProductViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getStock(), copy.getStock()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getSuppliersId(), copy.getSuppliersId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
