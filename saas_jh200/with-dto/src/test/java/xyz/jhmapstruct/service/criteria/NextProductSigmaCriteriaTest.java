package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextProductSigmaCriteriaTest {

    @Test
    void newNextProductSigmaCriteriaHasAllFiltersNullTest() {
        var nextProductSigmaCriteria = new NextProductSigmaCriteria();
        assertThat(nextProductSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextProductSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextProductSigmaCriteria = new NextProductSigmaCriteria();

        setAllFilters(nextProductSigmaCriteria);

        assertThat(nextProductSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextProductSigmaCriteriaCopyCreatesNullFilterTest() {
        var nextProductSigmaCriteria = new NextProductSigmaCriteria();
        var copy = nextProductSigmaCriteria.copy();

        assertThat(nextProductSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductSigmaCriteria)
        );
    }

    @Test
    void nextProductSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextProductSigmaCriteria = new NextProductSigmaCriteria();
        setAllFilters(nextProductSigmaCriteria);

        var copy = nextProductSigmaCriteria.copy();

        assertThat(nextProductSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextProductSigmaCriteria = new NextProductSigmaCriteria();

        assertThat(nextProductSigmaCriteria).hasToString("NextProductSigmaCriteria{}");
    }

    private static void setAllFilters(NextProductSigmaCriteria nextProductSigmaCriteria) {
        nextProductSigmaCriteria.id();
        nextProductSigmaCriteria.name();
        nextProductSigmaCriteria.price();
        nextProductSigmaCriteria.stock();
        nextProductSigmaCriteria.categoryId();
        nextProductSigmaCriteria.tenantId();
        nextProductSigmaCriteria.orderId();
        nextProductSigmaCriteria.suppliersId();
        nextProductSigmaCriteria.distinct();
    }

    private static Condition<NextProductSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextProductSigmaCriteria> copyFiltersAre(
        NextProductSigmaCriteria copy,
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
