package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PamelaLouisCriteriaTest {

    @Test
    void newPamelaLouisCriteriaHasAllFiltersNullTest() {
        var pamelaLouisCriteria = new PamelaLouisCriteria();
        assertThat(pamelaLouisCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void pamelaLouisCriteriaFluentMethodsCreatesFiltersTest() {
        var pamelaLouisCriteria = new PamelaLouisCriteria();

        setAllFilters(pamelaLouisCriteria);

        assertThat(pamelaLouisCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void pamelaLouisCriteriaCopyCreatesNullFilterTest() {
        var pamelaLouisCriteria = new PamelaLouisCriteria();
        var copy = pamelaLouisCriteria.copy();

        assertThat(pamelaLouisCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(pamelaLouisCriteria)
        );
    }

    @Test
    void pamelaLouisCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pamelaLouisCriteria = new PamelaLouisCriteria();
        setAllFilters(pamelaLouisCriteria);

        var copy = pamelaLouisCriteria.copy();

        assertThat(pamelaLouisCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(pamelaLouisCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pamelaLouisCriteria = new PamelaLouisCriteria();

        assertThat(pamelaLouisCriteria).hasToString("PamelaLouisCriteria{}");
    }

    private static void setAllFilters(PamelaLouisCriteria pamelaLouisCriteria) {
        pamelaLouisCriteria.id();
        pamelaLouisCriteria.name();
        pamelaLouisCriteria.configJason();
        pamelaLouisCriteria.distinct();
    }

    private static Condition<PamelaLouisCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getConfigJason()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PamelaLouisCriteria> copyFiltersAre(PamelaLouisCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
