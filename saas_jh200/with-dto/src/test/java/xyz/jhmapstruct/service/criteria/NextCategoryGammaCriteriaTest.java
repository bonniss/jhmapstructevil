package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCategoryGammaCriteriaTest {

    @Test
    void newNextCategoryGammaCriteriaHasAllFiltersNullTest() {
        var nextCategoryGammaCriteria = new NextCategoryGammaCriteria();
        assertThat(nextCategoryGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCategoryGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCategoryGammaCriteria = new NextCategoryGammaCriteria();

        setAllFilters(nextCategoryGammaCriteria);

        assertThat(nextCategoryGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCategoryGammaCriteriaCopyCreatesNullFilterTest() {
        var nextCategoryGammaCriteria = new NextCategoryGammaCriteria();
        var copy = nextCategoryGammaCriteria.copy();

        assertThat(nextCategoryGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryGammaCriteria)
        );
    }

    @Test
    void nextCategoryGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCategoryGammaCriteria = new NextCategoryGammaCriteria();
        setAllFilters(nextCategoryGammaCriteria);

        var copy = nextCategoryGammaCriteria.copy();

        assertThat(nextCategoryGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCategoryGammaCriteria = new NextCategoryGammaCriteria();

        assertThat(nextCategoryGammaCriteria).hasToString("NextCategoryGammaCriteria{}");
    }

    private static void setAllFilters(NextCategoryGammaCriteria nextCategoryGammaCriteria) {
        nextCategoryGammaCriteria.id();
        nextCategoryGammaCriteria.name();
        nextCategoryGammaCriteria.description();
        nextCategoryGammaCriteria.tenantId();
        nextCategoryGammaCriteria.distinct();
    }

    private static Condition<NextCategoryGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCategoryGammaCriteria> copyFiltersAre(
        NextCategoryGammaCriteria copy,
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
