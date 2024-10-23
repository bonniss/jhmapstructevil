package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CategoryThetaCriteriaTest {

    @Test
    void newCategoryThetaCriteriaHasAllFiltersNullTest() {
        var categoryThetaCriteria = new CategoryThetaCriteria();
        assertThat(categoryThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void categoryThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var categoryThetaCriteria = new CategoryThetaCriteria();

        setAllFilters(categoryThetaCriteria);

        assertThat(categoryThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void categoryThetaCriteriaCopyCreatesNullFilterTest() {
        var categoryThetaCriteria = new CategoryThetaCriteria();
        var copy = categoryThetaCriteria.copy();

        assertThat(categoryThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(categoryThetaCriteria)
        );
    }

    @Test
    void categoryThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var categoryThetaCriteria = new CategoryThetaCriteria();
        setAllFilters(categoryThetaCriteria);

        var copy = categoryThetaCriteria.copy();

        assertThat(categoryThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(categoryThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var categoryThetaCriteria = new CategoryThetaCriteria();

        assertThat(categoryThetaCriteria).hasToString("CategoryThetaCriteria{}");
    }

    private static void setAllFilters(CategoryThetaCriteria categoryThetaCriteria) {
        categoryThetaCriteria.id();
        categoryThetaCriteria.name();
        categoryThetaCriteria.description();
        categoryThetaCriteria.tenantId();
        categoryThetaCriteria.distinct();
    }

    private static Condition<CategoryThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CategoryThetaCriteria> copyFiltersAre(
        CategoryThetaCriteria copy,
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
