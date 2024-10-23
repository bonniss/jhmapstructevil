package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextProductBetaCriteriaTest {

    @Test
    void newNextProductBetaCriteriaHasAllFiltersNullTest() {
        var nextProductBetaCriteria = new NextProductBetaCriteria();
        assertThat(nextProductBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextProductBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextProductBetaCriteria = new NextProductBetaCriteria();

        setAllFilters(nextProductBetaCriteria);

        assertThat(nextProductBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextProductBetaCriteriaCopyCreatesNullFilterTest() {
        var nextProductBetaCriteria = new NextProductBetaCriteria();
        var copy = nextProductBetaCriteria.copy();

        assertThat(nextProductBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductBetaCriteria)
        );
    }

    @Test
    void nextProductBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextProductBetaCriteria = new NextProductBetaCriteria();
        setAllFilters(nextProductBetaCriteria);

        var copy = nextProductBetaCriteria.copy();

        assertThat(nextProductBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextProductBetaCriteria = new NextProductBetaCriteria();

        assertThat(nextProductBetaCriteria).hasToString("NextProductBetaCriteria{}");
    }

    private static void setAllFilters(NextProductBetaCriteria nextProductBetaCriteria) {
        nextProductBetaCriteria.id();
        nextProductBetaCriteria.name();
        nextProductBetaCriteria.price();
        nextProductBetaCriteria.stock();
        nextProductBetaCriteria.categoryId();
        nextProductBetaCriteria.tenantId();
        nextProductBetaCriteria.orderId();
        nextProductBetaCriteria.suppliersId();
        nextProductBetaCriteria.distinct();
    }

    private static Condition<NextProductBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextProductBetaCriteria> copyFiltersAre(
        NextProductBetaCriteria copy,
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
