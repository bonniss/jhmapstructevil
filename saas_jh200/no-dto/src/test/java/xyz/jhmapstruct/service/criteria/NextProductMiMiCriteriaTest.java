package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextProductMiMiCriteriaTest {

    @Test
    void newNextProductMiMiCriteriaHasAllFiltersNullTest() {
        var nextProductMiMiCriteria = new NextProductMiMiCriteria();
        assertThat(nextProductMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextProductMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextProductMiMiCriteria = new NextProductMiMiCriteria();

        setAllFilters(nextProductMiMiCriteria);

        assertThat(nextProductMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextProductMiMiCriteriaCopyCreatesNullFilterTest() {
        var nextProductMiMiCriteria = new NextProductMiMiCriteria();
        var copy = nextProductMiMiCriteria.copy();

        assertThat(nextProductMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductMiMiCriteria)
        );
    }

    @Test
    void nextProductMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextProductMiMiCriteria = new NextProductMiMiCriteria();
        setAllFilters(nextProductMiMiCriteria);

        var copy = nextProductMiMiCriteria.copy();

        assertThat(nextProductMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextProductMiMiCriteria = new NextProductMiMiCriteria();

        assertThat(nextProductMiMiCriteria).hasToString("NextProductMiMiCriteria{}");
    }

    private static void setAllFilters(NextProductMiMiCriteria nextProductMiMiCriteria) {
        nextProductMiMiCriteria.id();
        nextProductMiMiCriteria.name();
        nextProductMiMiCriteria.price();
        nextProductMiMiCriteria.stock();
        nextProductMiMiCriteria.categoryId();
        nextProductMiMiCriteria.tenantId();
        nextProductMiMiCriteria.orderId();
        nextProductMiMiCriteria.suppliersId();
        nextProductMiMiCriteria.distinct();
    }

    private static Condition<NextProductMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextProductMiMiCriteria> copyFiltersAre(
        NextProductMiMiCriteria copy,
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
