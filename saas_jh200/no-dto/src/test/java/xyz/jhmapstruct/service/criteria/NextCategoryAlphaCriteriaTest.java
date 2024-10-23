package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCategoryAlphaCriteriaTest {

    @Test
    void newNextCategoryAlphaCriteriaHasAllFiltersNullTest() {
        var nextCategoryAlphaCriteria = new NextCategoryAlphaCriteria();
        assertThat(nextCategoryAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCategoryAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCategoryAlphaCriteria = new NextCategoryAlphaCriteria();

        setAllFilters(nextCategoryAlphaCriteria);

        assertThat(nextCategoryAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCategoryAlphaCriteriaCopyCreatesNullFilterTest() {
        var nextCategoryAlphaCriteria = new NextCategoryAlphaCriteria();
        var copy = nextCategoryAlphaCriteria.copy();

        assertThat(nextCategoryAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryAlphaCriteria)
        );
    }

    @Test
    void nextCategoryAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCategoryAlphaCriteria = new NextCategoryAlphaCriteria();
        setAllFilters(nextCategoryAlphaCriteria);

        var copy = nextCategoryAlphaCriteria.copy();

        assertThat(nextCategoryAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCategoryAlphaCriteria = new NextCategoryAlphaCriteria();

        assertThat(nextCategoryAlphaCriteria).hasToString("NextCategoryAlphaCriteria{}");
    }

    private static void setAllFilters(NextCategoryAlphaCriteria nextCategoryAlphaCriteria) {
        nextCategoryAlphaCriteria.id();
        nextCategoryAlphaCriteria.name();
        nextCategoryAlphaCriteria.description();
        nextCategoryAlphaCriteria.tenantId();
        nextCategoryAlphaCriteria.distinct();
    }

    private static Condition<NextCategoryAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCategoryAlphaCriteria> copyFiltersAre(
        NextCategoryAlphaCriteria copy,
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
