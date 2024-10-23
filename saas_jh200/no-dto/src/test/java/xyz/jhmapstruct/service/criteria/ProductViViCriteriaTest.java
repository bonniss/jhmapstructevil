package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductViViCriteriaTest {

    @Test
    void newProductViViCriteriaHasAllFiltersNullTest() {
        var productViViCriteria = new ProductViViCriteria();
        assertThat(productViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productViViCriteriaFluentMethodsCreatesFiltersTest() {
        var productViViCriteria = new ProductViViCriteria();

        setAllFilters(productViViCriteria);

        assertThat(productViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productViViCriteriaCopyCreatesNullFilterTest() {
        var productViViCriteria = new ProductViViCriteria();
        var copy = productViViCriteria.copy();

        assertThat(productViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productViViCriteria)
        );
    }

    @Test
    void productViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productViViCriteria = new ProductViViCriteria();
        setAllFilters(productViViCriteria);

        var copy = productViViCriteria.copy();

        assertThat(productViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productViViCriteria = new ProductViViCriteria();

        assertThat(productViViCriteria).hasToString("ProductViViCriteria{}");
    }

    private static void setAllFilters(ProductViViCriteria productViViCriteria) {
        productViViCriteria.id();
        productViViCriteria.name();
        productViViCriteria.price();
        productViViCriteria.stock();
        productViViCriteria.categoryId();
        productViViCriteria.tenantId();
        productViViCriteria.orderId();
        productViViCriteria.suppliersId();
        productViViCriteria.distinct();
    }

    private static Condition<ProductViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ProductViViCriteria> copyFiltersAre(ProductViViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
