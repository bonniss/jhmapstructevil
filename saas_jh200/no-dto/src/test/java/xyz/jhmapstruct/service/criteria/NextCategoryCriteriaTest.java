package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCategoryCriteriaTest {

    @Test
    void newNextCategoryCriteriaHasAllFiltersNullTest() {
        var nextCategoryCriteria = new NextCategoryCriteria();
        assertThat(nextCategoryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCategoryCriteria = new NextCategoryCriteria();

        setAllFilters(nextCategoryCriteria);

        assertThat(nextCategoryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCategoryCriteriaCopyCreatesNullFilterTest() {
        var nextCategoryCriteria = new NextCategoryCriteria();
        var copy = nextCategoryCriteria.copy();

        assertThat(nextCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryCriteria)
        );
    }

    @Test
    void nextCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCategoryCriteria = new NextCategoryCriteria();
        setAllFilters(nextCategoryCriteria);

        var copy = nextCategoryCriteria.copy();

        assertThat(nextCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCategoryCriteria = new NextCategoryCriteria();

        assertThat(nextCategoryCriteria).hasToString("NextCategoryCriteria{}");
    }

    private static void setAllFilters(NextCategoryCriteria nextCategoryCriteria) {
        nextCategoryCriteria.id();
        nextCategoryCriteria.name();
        nextCategoryCriteria.description();
        nextCategoryCriteria.tenantId();
        nextCategoryCriteria.distinct();
    }

    private static Condition<NextCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCategoryCriteria> copyFiltersAre(
        NextCategoryCriteria copy,
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
