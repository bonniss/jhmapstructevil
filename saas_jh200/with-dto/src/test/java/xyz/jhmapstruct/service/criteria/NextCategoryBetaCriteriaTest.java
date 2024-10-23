package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCategoryBetaCriteriaTest {

    @Test
    void newNextCategoryBetaCriteriaHasAllFiltersNullTest() {
        var nextCategoryBetaCriteria = new NextCategoryBetaCriteria();
        assertThat(nextCategoryBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCategoryBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCategoryBetaCriteria = new NextCategoryBetaCriteria();

        setAllFilters(nextCategoryBetaCriteria);

        assertThat(nextCategoryBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCategoryBetaCriteriaCopyCreatesNullFilterTest() {
        var nextCategoryBetaCriteria = new NextCategoryBetaCriteria();
        var copy = nextCategoryBetaCriteria.copy();

        assertThat(nextCategoryBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryBetaCriteria)
        );
    }

    @Test
    void nextCategoryBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCategoryBetaCriteria = new NextCategoryBetaCriteria();
        setAllFilters(nextCategoryBetaCriteria);

        var copy = nextCategoryBetaCriteria.copy();

        assertThat(nextCategoryBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCategoryBetaCriteria = new NextCategoryBetaCriteria();

        assertThat(nextCategoryBetaCriteria).hasToString("NextCategoryBetaCriteria{}");
    }

    private static void setAllFilters(NextCategoryBetaCriteria nextCategoryBetaCriteria) {
        nextCategoryBetaCriteria.id();
        nextCategoryBetaCriteria.name();
        nextCategoryBetaCriteria.description();
        nextCategoryBetaCriteria.tenantId();
        nextCategoryBetaCriteria.distinct();
    }

    private static Condition<NextCategoryBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCategoryBetaCriteria> copyFiltersAre(
        NextCategoryBetaCriteria copy,
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
