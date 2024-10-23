package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextProductCriteriaTest {

    @Test
    void newNextProductCriteriaHasAllFiltersNullTest() {
        var nextProductCriteria = new NextProductCriteria();
        assertThat(nextProductCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextProductCriteriaFluentMethodsCreatesFiltersTest() {
        var nextProductCriteria = new NextProductCriteria();

        setAllFilters(nextProductCriteria);

        assertThat(nextProductCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextProductCriteriaCopyCreatesNullFilterTest() {
        var nextProductCriteria = new NextProductCriteria();
        var copy = nextProductCriteria.copy();

        assertThat(nextProductCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductCriteria)
        );
    }

    @Test
    void nextProductCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextProductCriteria = new NextProductCriteria();
        setAllFilters(nextProductCriteria);

        var copy = nextProductCriteria.copy();

        assertThat(nextProductCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextProductCriteria = new NextProductCriteria();

        assertThat(nextProductCriteria).hasToString("NextProductCriteria{}");
    }

    private static void setAllFilters(NextProductCriteria nextProductCriteria) {
        nextProductCriteria.id();
        nextProductCriteria.name();
        nextProductCriteria.price();
        nextProductCriteria.stock();
        nextProductCriteria.categoryId();
        nextProductCriteria.tenantId();
        nextProductCriteria.orderId();
        nextProductCriteria.suppliersId();
        nextProductCriteria.distinct();
    }

    private static Condition<NextProductCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextProductCriteria> copyFiltersAre(NextProductCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
