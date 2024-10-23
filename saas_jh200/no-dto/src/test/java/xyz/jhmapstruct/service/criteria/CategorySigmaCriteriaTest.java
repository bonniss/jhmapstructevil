package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CategorySigmaCriteriaTest {

    @Test
    void newCategorySigmaCriteriaHasAllFiltersNullTest() {
        var categorySigmaCriteria = new CategorySigmaCriteria();
        assertThat(categorySigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void categorySigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var categorySigmaCriteria = new CategorySigmaCriteria();

        setAllFilters(categorySigmaCriteria);

        assertThat(categorySigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void categorySigmaCriteriaCopyCreatesNullFilterTest() {
        var categorySigmaCriteria = new CategorySigmaCriteria();
        var copy = categorySigmaCriteria.copy();

        assertThat(categorySigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(categorySigmaCriteria)
        );
    }

    @Test
    void categorySigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var categorySigmaCriteria = new CategorySigmaCriteria();
        setAllFilters(categorySigmaCriteria);

        var copy = categorySigmaCriteria.copy();

        assertThat(categorySigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(categorySigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var categorySigmaCriteria = new CategorySigmaCriteria();

        assertThat(categorySigmaCriteria).hasToString("CategorySigmaCriteria{}");
    }

    private static void setAllFilters(CategorySigmaCriteria categorySigmaCriteria) {
        categorySigmaCriteria.id();
        categorySigmaCriteria.name();
        categorySigmaCriteria.description();
        categorySigmaCriteria.tenantId();
        categorySigmaCriteria.distinct();
    }

    private static Condition<CategorySigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CategorySigmaCriteria> copyFiltersAre(
        CategorySigmaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
