package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextProductViViCriteriaTest {

    @Test
    void newNextProductViViCriteriaHasAllFiltersNullTest() {
        var nextProductViViCriteria = new NextProductViViCriteria();
        assertThat(nextProductViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextProductViViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextProductViViCriteria = new NextProductViViCriteria();

        setAllFilters(nextProductViViCriteria);

        assertThat(nextProductViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextProductViViCriteriaCopyCreatesNullFilterTest() {
        var nextProductViViCriteria = new NextProductViViCriteria();
        var copy = nextProductViViCriteria.copy();

        assertThat(nextProductViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductViViCriteria)
        );
    }

    @Test
    void nextProductViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextProductViViCriteria = new NextProductViViCriteria();
        setAllFilters(nextProductViViCriteria);

        var copy = nextProductViViCriteria.copy();

        assertThat(nextProductViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextProductViViCriteria = new NextProductViViCriteria();

        assertThat(nextProductViViCriteria).hasToString("NextProductViViCriteria{}");
    }

    private static void setAllFilters(NextProductViViCriteria nextProductViViCriteria) {
        nextProductViViCriteria.id();
        nextProductViViCriteria.name();
        nextProductViViCriteria.price();
        nextProductViViCriteria.stock();
        nextProductViViCriteria.categoryId();
        nextProductViViCriteria.tenantId();
        nextProductViViCriteria.orderId();
        nextProductViViCriteria.suppliersId();
        nextProductViViCriteria.distinct();
    }

    private static Condition<NextProductViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextProductViViCriteria> copyFiltersAre(
        NextProductViViCriteria copy,
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