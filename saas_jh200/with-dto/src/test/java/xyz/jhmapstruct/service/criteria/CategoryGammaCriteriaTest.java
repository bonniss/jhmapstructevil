package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CategoryGammaCriteriaTest {

    @Test
    void newCategoryGammaCriteriaHasAllFiltersNullTest() {
        var categoryGammaCriteria = new CategoryGammaCriteria();
        assertThat(categoryGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void categoryGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var categoryGammaCriteria = new CategoryGammaCriteria();

        setAllFilters(categoryGammaCriteria);

        assertThat(categoryGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void categoryGammaCriteriaCopyCreatesNullFilterTest() {
        var categoryGammaCriteria = new CategoryGammaCriteria();
        var copy = categoryGammaCriteria.copy();

        assertThat(categoryGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(categoryGammaCriteria)
        );
    }

    @Test
    void categoryGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var categoryGammaCriteria = new CategoryGammaCriteria();
        setAllFilters(categoryGammaCriteria);

        var copy = categoryGammaCriteria.copy();

        assertThat(categoryGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(categoryGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var categoryGammaCriteria = new CategoryGammaCriteria();

        assertThat(categoryGammaCriteria).hasToString("CategoryGammaCriteria{}");
    }

    private static void setAllFilters(CategoryGammaCriteria categoryGammaCriteria) {
        categoryGammaCriteria.id();
        categoryGammaCriteria.name();
        categoryGammaCriteria.description();
        categoryGammaCriteria.tenantId();
        categoryGammaCriteria.distinct();
    }

    private static Condition<CategoryGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CategoryGammaCriteria> copyFiltersAre(
        CategoryGammaCriteria copy,
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
