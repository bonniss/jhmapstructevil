package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductMiCriteriaTest {

    @Test
    void newProductMiCriteriaHasAllFiltersNullTest() {
        var productMiCriteria = new ProductMiCriteria();
        assertThat(productMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productMiCriteriaFluentMethodsCreatesFiltersTest() {
        var productMiCriteria = new ProductMiCriteria();

        setAllFilters(productMiCriteria);

        assertThat(productMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productMiCriteriaCopyCreatesNullFilterTest() {
        var productMiCriteria = new ProductMiCriteria();
        var copy = productMiCriteria.copy();

        assertThat(productMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productMiCriteria)
        );
    }

    @Test
    void productMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productMiCriteria = new ProductMiCriteria();
        setAllFilters(productMiCriteria);

        var copy = productMiCriteria.copy();

        assertThat(productMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productMiCriteria = new ProductMiCriteria();

        assertThat(productMiCriteria).hasToString("ProductMiCriteria{}");
    }

    private static void setAllFilters(ProductMiCriteria productMiCriteria) {
        productMiCriteria.id();
        productMiCriteria.name();
        productMiCriteria.price();
        productMiCriteria.stock();
        productMiCriteria.categoryId();
        productMiCriteria.tenantId();
        productMiCriteria.orderId();
        productMiCriteria.suppliersId();
        productMiCriteria.distinct();
    }

    private static Condition<ProductMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ProductMiCriteria> copyFiltersAre(ProductMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
