package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextProductThetaCriteriaTest {

    @Test
    void newNextProductThetaCriteriaHasAllFiltersNullTest() {
        var nextProductThetaCriteria = new NextProductThetaCriteria();
        assertThat(nextProductThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextProductThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextProductThetaCriteria = new NextProductThetaCriteria();

        setAllFilters(nextProductThetaCriteria);

        assertThat(nextProductThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextProductThetaCriteriaCopyCreatesNullFilterTest() {
        var nextProductThetaCriteria = new NextProductThetaCriteria();
        var copy = nextProductThetaCriteria.copy();

        assertThat(nextProductThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductThetaCriteria)
        );
    }

    @Test
    void nextProductThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextProductThetaCriteria = new NextProductThetaCriteria();
        setAllFilters(nextProductThetaCriteria);

        var copy = nextProductThetaCriteria.copy();

        assertThat(nextProductThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextProductThetaCriteria = new NextProductThetaCriteria();

        assertThat(nextProductThetaCriteria).hasToString("NextProductThetaCriteria{}");
    }

    private static void setAllFilters(NextProductThetaCriteria nextProductThetaCriteria) {
        nextProductThetaCriteria.id();
        nextProductThetaCriteria.name();
        nextProductThetaCriteria.price();
        nextProductThetaCriteria.stock();
        nextProductThetaCriteria.categoryId();
        nextProductThetaCriteria.tenantId();
        nextProductThetaCriteria.orderId();
        nextProductThetaCriteria.suppliersId();
        nextProductThetaCriteria.distinct();
    }

    private static Condition<NextProductThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextProductThetaCriteria> copyFiltersAre(
        NextProductThetaCriteria copy,
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
