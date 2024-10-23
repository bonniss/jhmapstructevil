package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextProductGammaCriteriaTest {

    @Test
    void newNextProductGammaCriteriaHasAllFiltersNullTest() {
        var nextProductGammaCriteria = new NextProductGammaCriteria();
        assertThat(nextProductGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextProductGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextProductGammaCriteria = new NextProductGammaCriteria();

        setAllFilters(nextProductGammaCriteria);

        assertThat(nextProductGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextProductGammaCriteriaCopyCreatesNullFilterTest() {
        var nextProductGammaCriteria = new NextProductGammaCriteria();
        var copy = nextProductGammaCriteria.copy();

        assertThat(nextProductGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductGammaCriteria)
        );
    }

    @Test
    void nextProductGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextProductGammaCriteria = new NextProductGammaCriteria();
        setAllFilters(nextProductGammaCriteria);

        var copy = nextProductGammaCriteria.copy();

        assertThat(nextProductGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextProductGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextProductGammaCriteria = new NextProductGammaCriteria();

        assertThat(nextProductGammaCriteria).hasToString("NextProductGammaCriteria{}");
    }

    private static void setAllFilters(NextProductGammaCriteria nextProductGammaCriteria) {
        nextProductGammaCriteria.id();
        nextProductGammaCriteria.name();
        nextProductGammaCriteria.price();
        nextProductGammaCriteria.stock();
        nextProductGammaCriteria.categoryId();
        nextProductGammaCriteria.tenantId();
        nextProductGammaCriteria.orderId();
        nextProductGammaCriteria.suppliersId();
        nextProductGammaCriteria.distinct();
    }

    private static Condition<NextProductGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextProductGammaCriteria> copyFiltersAre(
        NextProductGammaCriteria copy,
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
