package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCategoryViViCriteriaTest {

    @Test
    void newNextCategoryViViCriteriaHasAllFiltersNullTest() {
        var nextCategoryViViCriteria = new NextCategoryViViCriteria();
        assertThat(nextCategoryViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCategoryViViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCategoryViViCriteria = new NextCategoryViViCriteria();

        setAllFilters(nextCategoryViViCriteria);

        assertThat(nextCategoryViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCategoryViViCriteriaCopyCreatesNullFilterTest() {
        var nextCategoryViViCriteria = new NextCategoryViViCriteria();
        var copy = nextCategoryViViCriteria.copy();

        assertThat(nextCategoryViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryViViCriteria)
        );
    }

    @Test
    void nextCategoryViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCategoryViViCriteria = new NextCategoryViViCriteria();
        setAllFilters(nextCategoryViViCriteria);

        var copy = nextCategoryViViCriteria.copy();

        assertThat(nextCategoryViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCategoryViViCriteria = new NextCategoryViViCriteria();

        assertThat(nextCategoryViViCriteria).hasToString("NextCategoryViViCriteria{}");
    }

    private static void setAllFilters(NextCategoryViViCriteria nextCategoryViViCriteria) {
        nextCategoryViViCriteria.id();
        nextCategoryViViCriteria.name();
        nextCategoryViViCriteria.description();
        nextCategoryViViCriteria.tenantId();
        nextCategoryViViCriteria.distinct();
    }

    private static Condition<NextCategoryViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCategoryViViCriteria> copyFiltersAre(
        NextCategoryViViCriteria copy,
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
