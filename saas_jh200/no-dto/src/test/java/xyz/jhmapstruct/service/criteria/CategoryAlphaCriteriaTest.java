package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CategoryAlphaCriteriaTest {

    @Test
    void newCategoryAlphaCriteriaHasAllFiltersNullTest() {
        var categoryAlphaCriteria = new CategoryAlphaCriteria();
        assertThat(categoryAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void categoryAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var categoryAlphaCriteria = new CategoryAlphaCriteria();

        setAllFilters(categoryAlphaCriteria);

        assertThat(categoryAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void categoryAlphaCriteriaCopyCreatesNullFilterTest() {
        var categoryAlphaCriteria = new CategoryAlphaCriteria();
        var copy = categoryAlphaCriteria.copy();

        assertThat(categoryAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(categoryAlphaCriteria)
        );
    }

    @Test
    void categoryAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var categoryAlphaCriteria = new CategoryAlphaCriteria();
        setAllFilters(categoryAlphaCriteria);

        var copy = categoryAlphaCriteria.copy();

        assertThat(categoryAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(categoryAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var categoryAlphaCriteria = new CategoryAlphaCriteria();

        assertThat(categoryAlphaCriteria).hasToString("CategoryAlphaCriteria{}");
    }

    private static void setAllFilters(CategoryAlphaCriteria categoryAlphaCriteria) {
        categoryAlphaCriteria.id();
        categoryAlphaCriteria.name();
        categoryAlphaCriteria.description();
        categoryAlphaCriteria.tenantId();
        categoryAlphaCriteria.distinct();
    }

    private static Condition<CategoryAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CategoryAlphaCriteria> copyFiltersAre(
        CategoryAlphaCriteria copy,
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
