package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCategoryMiMiCriteriaTest {

    @Test
    void newNextCategoryMiMiCriteriaHasAllFiltersNullTest() {
        var nextCategoryMiMiCriteria = new NextCategoryMiMiCriteria();
        assertThat(nextCategoryMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCategoryMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCategoryMiMiCriteria = new NextCategoryMiMiCriteria();

        setAllFilters(nextCategoryMiMiCriteria);

        assertThat(nextCategoryMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCategoryMiMiCriteriaCopyCreatesNullFilterTest() {
        var nextCategoryMiMiCriteria = new NextCategoryMiMiCriteria();
        var copy = nextCategoryMiMiCriteria.copy();

        assertThat(nextCategoryMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryMiMiCriteria)
        );
    }

    @Test
    void nextCategoryMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCategoryMiMiCriteria = new NextCategoryMiMiCriteria();
        setAllFilters(nextCategoryMiMiCriteria);

        var copy = nextCategoryMiMiCriteria.copy();

        assertThat(nextCategoryMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCategoryMiMiCriteria = new NextCategoryMiMiCriteria();

        assertThat(nextCategoryMiMiCriteria).hasToString("NextCategoryMiMiCriteria{}");
    }

    private static void setAllFilters(NextCategoryMiMiCriteria nextCategoryMiMiCriteria) {
        nextCategoryMiMiCriteria.id();
        nextCategoryMiMiCriteria.name();
        nextCategoryMiMiCriteria.description();
        nextCategoryMiMiCriteria.tenantId();
        nextCategoryMiMiCriteria.distinct();
    }

    private static Condition<NextCategoryMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCategoryMiMiCriteria> copyFiltersAre(
        NextCategoryMiMiCriteria copy,
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
