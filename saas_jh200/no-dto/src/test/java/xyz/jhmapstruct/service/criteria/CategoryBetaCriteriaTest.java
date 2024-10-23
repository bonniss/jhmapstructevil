package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CategoryBetaCriteriaTest {

    @Test
    void newCategoryBetaCriteriaHasAllFiltersNullTest() {
        var categoryBetaCriteria = new CategoryBetaCriteria();
        assertThat(categoryBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void categoryBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var categoryBetaCriteria = new CategoryBetaCriteria();

        setAllFilters(categoryBetaCriteria);

        assertThat(categoryBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void categoryBetaCriteriaCopyCreatesNullFilterTest() {
        var categoryBetaCriteria = new CategoryBetaCriteria();
        var copy = categoryBetaCriteria.copy();

        assertThat(categoryBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(categoryBetaCriteria)
        );
    }

    @Test
    void categoryBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var categoryBetaCriteria = new CategoryBetaCriteria();
        setAllFilters(categoryBetaCriteria);

        var copy = categoryBetaCriteria.copy();

        assertThat(categoryBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(categoryBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var categoryBetaCriteria = new CategoryBetaCriteria();

        assertThat(categoryBetaCriteria).hasToString("CategoryBetaCriteria{}");
    }

    private static void setAllFilters(CategoryBetaCriteria categoryBetaCriteria) {
        categoryBetaCriteria.id();
        categoryBetaCriteria.name();
        categoryBetaCriteria.description();
        categoryBetaCriteria.tenantId();
        categoryBetaCriteria.distinct();
    }

    private static Condition<CategoryBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CategoryBetaCriteria> copyFiltersAre(
        CategoryBetaCriteria copy,
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
