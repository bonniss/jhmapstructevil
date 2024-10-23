package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductBetaCriteriaTest {

    @Test
    void newProductBetaCriteriaHasAllFiltersNullTest() {
        var productBetaCriteria = new ProductBetaCriteria();
        assertThat(productBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var productBetaCriteria = new ProductBetaCriteria();

        setAllFilters(productBetaCriteria);

        assertThat(productBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productBetaCriteriaCopyCreatesNullFilterTest() {
        var productBetaCriteria = new ProductBetaCriteria();
        var copy = productBetaCriteria.copy();

        assertThat(productBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productBetaCriteria)
        );
    }

    @Test
    void productBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productBetaCriteria = new ProductBetaCriteria();
        setAllFilters(productBetaCriteria);

        var copy = productBetaCriteria.copy();

        assertThat(productBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productBetaCriteria = new ProductBetaCriteria();

        assertThat(productBetaCriteria).hasToString("ProductBetaCriteria{}");
    }

    private static void setAllFilters(ProductBetaCriteria productBetaCriteria) {
        productBetaCriteria.id();
        productBetaCriteria.name();
        productBetaCriteria.price();
        productBetaCriteria.stock();
        productBetaCriteria.categoryId();
        productBetaCriteria.tenantId();
        productBetaCriteria.orderId();
        productBetaCriteria.suppliersId();
        productBetaCriteria.distinct();
    }

    private static Condition<ProductBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ProductBetaCriteria> copyFiltersAre(ProductBetaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
