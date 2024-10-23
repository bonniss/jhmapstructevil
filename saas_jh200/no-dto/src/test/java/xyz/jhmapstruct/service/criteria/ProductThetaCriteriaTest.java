package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductThetaCriteriaTest {

    @Test
    void newProductThetaCriteriaHasAllFiltersNullTest() {
        var productThetaCriteria = new ProductThetaCriteria();
        assertThat(productThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var productThetaCriteria = new ProductThetaCriteria();

        setAllFilters(productThetaCriteria);

        assertThat(productThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productThetaCriteriaCopyCreatesNullFilterTest() {
        var productThetaCriteria = new ProductThetaCriteria();
        var copy = productThetaCriteria.copy();

        assertThat(productThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productThetaCriteria)
        );
    }

    @Test
    void productThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productThetaCriteria = new ProductThetaCriteria();
        setAllFilters(productThetaCriteria);

        var copy = productThetaCriteria.copy();

        assertThat(productThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productThetaCriteria = new ProductThetaCriteria();

        assertThat(productThetaCriteria).hasToString("ProductThetaCriteria{}");
    }

    private static void setAllFilters(ProductThetaCriteria productThetaCriteria) {
        productThetaCriteria.id();
        productThetaCriteria.name();
        productThetaCriteria.price();
        productThetaCriteria.stock();
        productThetaCriteria.categoryId();
        productThetaCriteria.tenantId();
        productThetaCriteria.orderId();
        productThetaCriteria.suppliersId();
        productThetaCriteria.distinct();
    }

    private static Condition<ProductThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ProductThetaCriteria> copyFiltersAre(
        ProductThetaCriteria copy,
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
