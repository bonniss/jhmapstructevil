package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductGammaCriteriaTest {

    @Test
    void newProductGammaCriteriaHasAllFiltersNullTest() {
        var productGammaCriteria = new ProductGammaCriteria();
        assertThat(productGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var productGammaCriteria = new ProductGammaCriteria();

        setAllFilters(productGammaCriteria);

        assertThat(productGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productGammaCriteriaCopyCreatesNullFilterTest() {
        var productGammaCriteria = new ProductGammaCriteria();
        var copy = productGammaCriteria.copy();

        assertThat(productGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productGammaCriteria)
        );
    }

    @Test
    void productGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productGammaCriteria = new ProductGammaCriteria();
        setAllFilters(productGammaCriteria);

        var copy = productGammaCriteria.copy();

        assertThat(productGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productGammaCriteria = new ProductGammaCriteria();

        assertThat(productGammaCriteria).hasToString("ProductGammaCriteria{}");
    }

    private static void setAllFilters(ProductGammaCriteria productGammaCriteria) {
        productGammaCriteria.id();
        productGammaCriteria.name();
        productGammaCriteria.price();
        productGammaCriteria.stock();
        productGammaCriteria.categoryId();
        productGammaCriteria.tenantId();
        productGammaCriteria.orderId();
        productGammaCriteria.suppliersId();
        productGammaCriteria.distinct();
    }

    private static Condition<ProductGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ProductGammaCriteria> copyFiltersAre(
        ProductGammaCriteria copy,
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
