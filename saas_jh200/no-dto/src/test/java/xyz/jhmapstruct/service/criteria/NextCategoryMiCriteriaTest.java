package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCategoryMiCriteriaTest {

    @Test
    void newNextCategoryMiCriteriaHasAllFiltersNullTest() {
        var nextCategoryMiCriteria = new NextCategoryMiCriteria();
        assertThat(nextCategoryMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCategoryMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCategoryMiCriteria = new NextCategoryMiCriteria();

        setAllFilters(nextCategoryMiCriteria);

        assertThat(nextCategoryMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCategoryMiCriteriaCopyCreatesNullFilterTest() {
        var nextCategoryMiCriteria = new NextCategoryMiCriteria();
        var copy = nextCategoryMiCriteria.copy();

        assertThat(nextCategoryMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryMiCriteria)
        );
    }

    @Test
    void nextCategoryMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCategoryMiCriteria = new NextCategoryMiCriteria();
        setAllFilters(nextCategoryMiCriteria);

        var copy = nextCategoryMiCriteria.copy();

        assertThat(nextCategoryMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCategoryMiCriteria = new NextCategoryMiCriteria();

        assertThat(nextCategoryMiCriteria).hasToString("NextCategoryMiCriteria{}");
    }

    private static void setAllFilters(NextCategoryMiCriteria nextCategoryMiCriteria) {
        nextCategoryMiCriteria.id();
        nextCategoryMiCriteria.name();
        nextCategoryMiCriteria.description();
        nextCategoryMiCriteria.tenantId();
        nextCategoryMiCriteria.distinct();
    }

    private static Condition<NextCategoryMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCategoryMiCriteria> copyFiltersAre(
        NextCategoryMiCriteria copy,
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
