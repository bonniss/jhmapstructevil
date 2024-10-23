package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CategoryViCriteriaTest {

    @Test
    void newCategoryViCriteriaHasAllFiltersNullTest() {
        var categoryViCriteria = new CategoryViCriteria();
        assertThat(categoryViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void categoryViCriteriaFluentMethodsCreatesFiltersTest() {
        var categoryViCriteria = new CategoryViCriteria();

        setAllFilters(categoryViCriteria);

        assertThat(categoryViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void categoryViCriteriaCopyCreatesNullFilterTest() {
        var categoryViCriteria = new CategoryViCriteria();
        var copy = categoryViCriteria.copy();

        assertThat(categoryViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(categoryViCriteria)
        );
    }

    @Test
    void categoryViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var categoryViCriteria = new CategoryViCriteria();
        setAllFilters(categoryViCriteria);

        var copy = categoryViCriteria.copy();

        assertThat(categoryViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(categoryViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var categoryViCriteria = new CategoryViCriteria();

        assertThat(categoryViCriteria).hasToString("CategoryViCriteria{}");
    }

    private static void setAllFilters(CategoryViCriteria categoryViCriteria) {
        categoryViCriteria.id();
        categoryViCriteria.name();
        categoryViCriteria.description();
        categoryViCriteria.tenantId();
        categoryViCriteria.distinct();
    }

    private static Condition<CategoryViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CategoryViCriteria> copyFiltersAre(CategoryViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
