package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCategoryViCriteriaTest {

    @Test
    void newNextCategoryViCriteriaHasAllFiltersNullTest() {
        var nextCategoryViCriteria = new NextCategoryViCriteria();
        assertThat(nextCategoryViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCategoryViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCategoryViCriteria = new NextCategoryViCriteria();

        setAllFilters(nextCategoryViCriteria);

        assertThat(nextCategoryViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCategoryViCriteriaCopyCreatesNullFilterTest() {
        var nextCategoryViCriteria = new NextCategoryViCriteria();
        var copy = nextCategoryViCriteria.copy();

        assertThat(nextCategoryViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryViCriteria)
        );
    }

    @Test
    void nextCategoryViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCategoryViCriteria = new NextCategoryViCriteria();
        setAllFilters(nextCategoryViCriteria);

        var copy = nextCategoryViCriteria.copy();

        assertThat(nextCategoryViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCategoryViCriteria = new NextCategoryViCriteria();

        assertThat(nextCategoryViCriteria).hasToString("NextCategoryViCriteria{}");
    }

    private static void setAllFilters(NextCategoryViCriteria nextCategoryViCriteria) {
        nextCategoryViCriteria.id();
        nextCategoryViCriteria.name();
        nextCategoryViCriteria.description();
        nextCategoryViCriteria.tenantId();
        nextCategoryViCriteria.distinct();
    }

    private static Condition<NextCategoryViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCategoryViCriteria> copyFiltersAre(
        NextCategoryViCriteria copy,
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
