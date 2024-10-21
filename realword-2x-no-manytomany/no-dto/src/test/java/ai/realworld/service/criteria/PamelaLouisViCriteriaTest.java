package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PamelaLouisViCriteriaTest {

    @Test
    void newPamelaLouisViCriteriaHasAllFiltersNullTest() {
        var pamelaLouisViCriteria = new PamelaLouisViCriteria();
        assertThat(pamelaLouisViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void pamelaLouisViCriteriaFluentMethodsCreatesFiltersTest() {
        var pamelaLouisViCriteria = new PamelaLouisViCriteria();

        setAllFilters(pamelaLouisViCriteria);

        assertThat(pamelaLouisViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void pamelaLouisViCriteriaCopyCreatesNullFilterTest() {
        var pamelaLouisViCriteria = new PamelaLouisViCriteria();
        var copy = pamelaLouisViCriteria.copy();

        assertThat(pamelaLouisViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(pamelaLouisViCriteria)
        );
    }

    @Test
    void pamelaLouisViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pamelaLouisViCriteria = new PamelaLouisViCriteria();
        setAllFilters(pamelaLouisViCriteria);

        var copy = pamelaLouisViCriteria.copy();

        assertThat(pamelaLouisViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(pamelaLouisViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pamelaLouisViCriteria = new PamelaLouisViCriteria();

        assertThat(pamelaLouisViCriteria).hasToString("PamelaLouisViCriteria{}");
    }

    private static void setAllFilters(PamelaLouisViCriteria pamelaLouisViCriteria) {
        pamelaLouisViCriteria.id();
        pamelaLouisViCriteria.name();
        pamelaLouisViCriteria.configJason();
        pamelaLouisViCriteria.distinct();
    }

    private static Condition<PamelaLouisViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getConfigJason()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PamelaLouisViCriteria> copyFiltersAre(
        PamelaLouisViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getConfigJason(), copy.getConfigJason()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
