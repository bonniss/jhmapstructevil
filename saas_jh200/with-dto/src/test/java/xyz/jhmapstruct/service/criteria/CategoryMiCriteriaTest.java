package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CategoryMiCriteriaTest {

    @Test
    void newCategoryMiCriteriaHasAllFiltersNullTest() {
        var categoryMiCriteria = new CategoryMiCriteria();
        assertThat(categoryMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void categoryMiCriteriaFluentMethodsCreatesFiltersTest() {
        var categoryMiCriteria = new CategoryMiCriteria();

        setAllFilters(categoryMiCriteria);

        assertThat(categoryMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void categoryMiCriteriaCopyCreatesNullFilterTest() {
        var categoryMiCriteria = new CategoryMiCriteria();
        var copy = categoryMiCriteria.copy();

        assertThat(categoryMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(categoryMiCriteria)
        );
    }

    @Test
    void categoryMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var categoryMiCriteria = new CategoryMiCriteria();
        setAllFilters(categoryMiCriteria);

        var copy = categoryMiCriteria.copy();

        assertThat(categoryMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(categoryMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var categoryMiCriteria = new CategoryMiCriteria();

        assertThat(categoryMiCriteria).hasToString("CategoryMiCriteria{}");
    }

    private static void setAllFilters(CategoryMiCriteria categoryMiCriteria) {
        categoryMiCriteria.id();
        categoryMiCriteria.name();
        categoryMiCriteria.description();
        categoryMiCriteria.tenantId();
        categoryMiCriteria.distinct();
    }

    private static Condition<CategoryMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CategoryMiCriteria> copyFiltersAre(CategoryMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
