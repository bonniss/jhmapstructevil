package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextProductMiCriteriaTest {

    @Test
    void newNextProductMiCriteriaHasAllFiltersNullTest() {
        var nextProductMiCriteria = new NextProductMiCriteria();
        assertThat(nextProductMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextProductMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextProductMiCriteria = new NextProductMiCriteria();

        setAllFilters(nextProductMiCriteria);

        assertThat(nextProductMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextProductMiCriteriaCopyCreatesNullFilterTest() {
        var nextProductMiCriteria = new NextProductMiCriteria();
        var copy = nextProductMiCriteria.copy();

        assertThat(nextProductMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductMiCriteria)
        );
    }

    @Test
    void nextProductMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextProductMiCriteria = new NextProductMiCriteria();
        setAllFilters(nextProductMiCriteria);

        var copy = nextProductMiCriteria.copy();

        assertThat(nextProductMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextProductMiCriteria = new NextProductMiCriteria();

        assertThat(nextProductMiCriteria).hasToString("NextProductMiCriteria{}");
    }

    private static void setAllFilters(NextProductMiCriteria nextProductMiCriteria) {
        nextProductMiCriteria.id();
        nextProductMiCriteria.name();
        nextProductMiCriteria.price();
        nextProductMiCriteria.stock();
        nextProductMiCriteria.categoryId();
        nextProductMiCriteria.tenantId();
        nextProductMiCriteria.orderId();
        nextProductMiCriteria.suppliersId();
        nextProductMiCriteria.distinct();
    }

    private static Condition<NextProductMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextProductMiCriteria> copyFiltersAre(
        NextProductMiCriteria copy,
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
