package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductViCriteriaTest {

    @Test
    void newProductViCriteriaHasAllFiltersNullTest() {
        var productViCriteria = new ProductViCriteria();
        assertThat(productViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productViCriteriaFluentMethodsCreatesFiltersTest() {
        var productViCriteria = new ProductViCriteria();

        setAllFilters(productViCriteria);

        assertThat(productViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productViCriteriaCopyCreatesNullFilterTest() {
        var productViCriteria = new ProductViCriteria();
        var copy = productViCriteria.copy();

        assertThat(productViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productViCriteria)
        );
    }

    @Test
    void productViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productViCriteria = new ProductViCriteria();
        setAllFilters(productViCriteria);

        var copy = productViCriteria.copy();

        assertThat(productViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productViCriteria = new ProductViCriteria();

        assertThat(productViCriteria).hasToString("ProductViCriteria{}");
    }

    private static void setAllFilters(ProductViCriteria productViCriteria) {
        productViCriteria.id();
        productViCriteria.name();
        productViCriteria.price();
        productViCriteria.stock();
        productViCriteria.categoryId();
        productViCriteria.tenantId();
        productViCriteria.orderId();
        productViCriteria.suppliersId();
        productViCriteria.distinct();
    }

    private static Condition<ProductViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ProductViCriteria> copyFiltersAre(ProductViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
