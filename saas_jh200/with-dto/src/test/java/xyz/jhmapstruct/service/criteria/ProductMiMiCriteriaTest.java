package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductMiMiCriteriaTest {

    @Test
    void newProductMiMiCriteriaHasAllFiltersNullTest() {
        var productMiMiCriteria = new ProductMiMiCriteria();
        assertThat(productMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var productMiMiCriteria = new ProductMiMiCriteria();

        setAllFilters(productMiMiCriteria);

        assertThat(productMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productMiMiCriteriaCopyCreatesNullFilterTest() {
        var productMiMiCriteria = new ProductMiMiCriteria();
        var copy = productMiMiCriteria.copy();

        assertThat(productMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productMiMiCriteria)
        );
    }

    @Test
    void productMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productMiMiCriteria = new ProductMiMiCriteria();
        setAllFilters(productMiMiCriteria);

        var copy = productMiMiCriteria.copy();

        assertThat(productMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productMiMiCriteria = new ProductMiMiCriteria();

        assertThat(productMiMiCriteria).hasToString("ProductMiMiCriteria{}");
    }

    private static void setAllFilters(ProductMiMiCriteria productMiMiCriteria) {
        productMiMiCriteria.id();
        productMiMiCriteria.name();
        productMiMiCriteria.price();
        productMiMiCriteria.stock();
        productMiMiCriteria.categoryId();
        productMiMiCriteria.tenantId();
        productMiMiCriteria.orderId();
        productMiMiCriteria.suppliersId();
        productMiMiCriteria.distinct();
    }

    private static Condition<ProductMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ProductMiMiCriteria> copyFiltersAre(ProductMiMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
