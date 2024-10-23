package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CategoryMiMiCriteriaTest {

    @Test
    void newCategoryMiMiCriteriaHasAllFiltersNullTest() {
        var categoryMiMiCriteria = new CategoryMiMiCriteria();
        assertThat(categoryMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void categoryMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var categoryMiMiCriteria = new CategoryMiMiCriteria();

        setAllFilters(categoryMiMiCriteria);

        assertThat(categoryMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void categoryMiMiCriteriaCopyCreatesNullFilterTest() {
        var categoryMiMiCriteria = new CategoryMiMiCriteria();
        var copy = categoryMiMiCriteria.copy();

        assertThat(categoryMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(categoryMiMiCriteria)
        );
    }

    @Test
    void categoryMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var categoryMiMiCriteria = new CategoryMiMiCriteria();
        setAllFilters(categoryMiMiCriteria);

        var copy = categoryMiMiCriteria.copy();

        assertThat(categoryMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(categoryMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var categoryMiMiCriteria = new CategoryMiMiCriteria();

        assertThat(categoryMiMiCriteria).hasToString("CategoryMiMiCriteria{}");
    }

    private static void setAllFilters(CategoryMiMiCriteria categoryMiMiCriteria) {
        categoryMiMiCriteria.id();
        categoryMiMiCriteria.name();
        categoryMiMiCriteria.description();
        categoryMiMiCriteria.tenantId();
        categoryMiMiCriteria.distinct();
    }

    private static Condition<CategoryMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CategoryMiMiCriteria> copyFiltersAre(
        CategoryMiMiCriteria copy,
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
