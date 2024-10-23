package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextCategoryThetaCriteriaTest {

    @Test
    void newNextCategoryThetaCriteriaHasAllFiltersNullTest() {
        var nextCategoryThetaCriteria = new NextCategoryThetaCriteria();
        assertThat(nextCategoryThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextCategoryThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextCategoryThetaCriteria = new NextCategoryThetaCriteria();

        setAllFilters(nextCategoryThetaCriteria);

        assertThat(nextCategoryThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextCategoryThetaCriteriaCopyCreatesNullFilterTest() {
        var nextCategoryThetaCriteria = new NextCategoryThetaCriteria();
        var copy = nextCategoryThetaCriteria.copy();

        assertThat(nextCategoryThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryThetaCriteria)
        );
    }

    @Test
    void nextCategoryThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextCategoryThetaCriteria = new NextCategoryThetaCriteria();
        setAllFilters(nextCategoryThetaCriteria);

        var copy = nextCategoryThetaCriteria.copy();

        assertThat(nextCategoryThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextCategoryThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextCategoryThetaCriteria = new NextCategoryThetaCriteria();

        assertThat(nextCategoryThetaCriteria).hasToString("NextCategoryThetaCriteria{}");
    }

    private static void setAllFilters(NextCategoryThetaCriteria nextCategoryThetaCriteria) {
        nextCategoryThetaCriteria.id();
        nextCategoryThetaCriteria.name();
        nextCategoryThetaCriteria.description();
        nextCategoryThetaCriteria.tenantId();
        nextCategoryThetaCriteria.distinct();
    }

    private static Condition<NextCategoryThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextCategoryThetaCriteria> copyFiltersAre(
        NextCategoryThetaCriteria copy,
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
