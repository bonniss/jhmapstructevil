package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductSigmaCriteriaTest {

    @Test
    void newProductSigmaCriteriaHasAllFiltersNullTest() {
        var productSigmaCriteria = new ProductSigmaCriteria();
        assertThat(productSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var productSigmaCriteria = new ProductSigmaCriteria();

        setAllFilters(productSigmaCriteria);

        assertThat(productSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productSigmaCriteriaCopyCreatesNullFilterTest() {
        var productSigmaCriteria = new ProductSigmaCriteria();
        var copy = productSigmaCriteria.copy();

        assertThat(productSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productSigmaCriteria)
        );
    }

    @Test
    void productSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productSigmaCriteria = new ProductSigmaCriteria();
        setAllFilters(productSigmaCriteria);

        var copy = productSigmaCriteria.copy();

        assertThat(productSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productSigmaCriteria = new ProductSigmaCriteria();

        assertThat(productSigmaCriteria).hasToString("ProductSigmaCriteria{}");
    }

    private static void setAllFilters(ProductSigmaCriteria productSigmaCriteria) {
        productSigmaCriteria.id();
        productSigmaCriteria.name();
        productSigmaCriteria.price();
        productSigmaCriteria.stock();
        productSigmaCriteria.categoryId();
        productSigmaCriteria.tenantId();
        productSigmaCriteria.orderId();
        productSigmaCriteria.suppliersId();
        productSigmaCriteria.distinct();
    }

    private static Condition<ProductSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ProductSigmaCriteria> copyFiltersAre(
        ProductSigmaCriteria copy,
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
