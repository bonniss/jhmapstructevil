package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextProductAlphaCriteriaTest {

    @Test
    void newNextProductAlphaCriteriaHasAllFiltersNullTest() {
        var nextProductAlphaCriteria = new NextProductAlphaCriteria();
        assertThat(nextProductAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextProductAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextProductAlphaCriteria = new NextProductAlphaCriteria();

        setAllFilters(nextProductAlphaCriteria);

        assertThat(nextProductAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextProductAlphaCriteriaCopyCreatesNullFilterTest() {
        var nextProductAlphaCriteria = new NextProductAlphaCriteria();
        var copy = nextProductAlphaCriteria.copy();

        assertThat(nextProductAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductAlphaCriteria)
        );
    }

    @Test
    void nextProductAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextProductAlphaCriteria = new NextProductAlphaCriteria();
        setAllFilters(nextProductAlphaCriteria);

        var copy = nextProductAlphaCriteria.copy();

        assertThat(nextProductAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextProductAlphaCriteria = new NextProductAlphaCriteria();

        assertThat(nextProductAlphaCriteria).hasToString("NextProductAlphaCriteria{}");
    }

    private static void setAllFilters(NextProductAlphaCriteria nextProductAlphaCriteria) {
        nextProductAlphaCriteria.id();
        nextProductAlphaCriteria.name();
        nextProductAlphaCriteria.price();
        nextProductAlphaCriteria.stock();
        nextProductAlphaCriteria.categoryId();
        nextProductAlphaCriteria.tenantId();
        nextProductAlphaCriteria.orderId();
        nextProductAlphaCriteria.suppliersId();
        nextProductAlphaCriteria.distinct();
    }

    private static Condition<NextProductAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextProductAlphaCriteria> copyFiltersAre(
        NextProductAlphaCriteria copy,
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
