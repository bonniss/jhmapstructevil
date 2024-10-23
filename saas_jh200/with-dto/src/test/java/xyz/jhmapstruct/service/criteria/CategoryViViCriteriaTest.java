package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CategoryViViCriteriaTest {

    @Test
    void newCategoryViViCriteriaHasAllFiltersNullTest() {
        var categoryViViCriteria = new CategoryViViCriteria();
        assertThat(categoryViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void categoryViViCriteriaFluentMethodsCreatesFiltersTest() {
        var categoryViViCriteria = new CategoryViViCriteria();

        setAllFilters(categoryViViCriteria);

        assertThat(categoryViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void categoryViViCriteriaCopyCreatesNullFilterTest() {
        var categoryViViCriteria = new CategoryViViCriteria();
        var copy = categoryViViCriteria.copy();

        assertThat(categoryViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(categoryViViCriteria)
        );
    }

    @Test
    void categoryViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var categoryViViCriteria = new CategoryViViCriteria();
        setAllFilters(categoryViViCriteria);

        var copy = categoryViViCriteria.copy();

        assertThat(categoryViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(categoryViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var categoryViViCriteria = new CategoryViViCriteria();

        assertThat(categoryViViCriteria).hasToString("CategoryViViCriteria{}");
    }

    private static void setAllFilters(CategoryViViCriteria categoryViViCriteria) {
        categoryViViCriteria.id();
        categoryViViCriteria.name();
        categoryViViCriteria.description();
        categoryViViCriteria.tenantId();
        categoryViViCriteria.distinct();
    }

    private static Condition<CategoryViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CategoryViViCriteria> copyFiltersAre(
        CategoryViViCriteria copy,
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
