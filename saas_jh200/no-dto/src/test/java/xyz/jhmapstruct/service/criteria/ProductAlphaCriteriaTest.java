package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductAlphaCriteriaTest {

    @Test
    void newProductAlphaCriteriaHasAllFiltersNullTest() {
        var productAlphaCriteria = new ProductAlphaCriteria();
        assertThat(productAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var productAlphaCriteria = new ProductAlphaCriteria();

        setAllFilters(productAlphaCriteria);

        assertThat(productAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productAlphaCriteriaCopyCreatesNullFilterTest() {
        var productAlphaCriteria = new ProductAlphaCriteria();
        var copy = productAlphaCriteria.copy();

        assertThat(productAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productAlphaCriteria)
        );
    }

    @Test
    void productAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productAlphaCriteria = new ProductAlphaCriteria();
        setAllFilters(productAlphaCriteria);

        var copy = productAlphaCriteria.copy();

        assertThat(productAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productAlphaCriteria = new ProductAlphaCriteria();

        assertThat(productAlphaCriteria).hasToString("ProductAlphaCriteria{}");
    }

    private static void setAllFilters(ProductAlphaCriteria productAlphaCriteria) {
        productAlphaCriteria.id();
        productAlphaCriteria.name();
        productAlphaCriteria.price();
        productAlphaCriteria.stock();
        productAlphaCriteria.categoryId();
        productAlphaCriteria.tenantId();
        productAlphaCriteria.orderId();
        productAlphaCriteria.suppliersId();
        productAlphaCriteria.distinct();
    }

    private static Condition<ProductAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ProductAlphaCriteria> copyFiltersAre(
        ProductAlphaCriteria copy,
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