package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SaisanCogViCriteriaTest {

    @Test
    void newSaisanCogViCriteriaHasAllFiltersNullTest() {
        var saisanCogViCriteria = new SaisanCogViCriteria();
        assertThat(saisanCogViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void saisanCogViCriteriaFluentMethodsCreatesFiltersTest() {
        var saisanCogViCriteria = new SaisanCogViCriteria();

        setAllFilters(saisanCogViCriteria);

        assertThat(saisanCogViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void saisanCogViCriteriaCopyCreatesNullFilterTest() {
        var saisanCogViCriteria = new SaisanCogViCriteria();
        var copy = saisanCogViCriteria.copy();

        assertThat(saisanCogViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(saisanCogViCriteria)
        );
    }

    @Test
    void saisanCogViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var saisanCogViCriteria = new SaisanCogViCriteria();
        setAllFilters(saisanCogViCriteria);

        var copy = saisanCogViCriteria.copy();

        assertThat(saisanCogViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(saisanCogViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var saisanCogViCriteria = new SaisanCogViCriteria();

        assertThat(saisanCogViCriteria).hasToString("SaisanCogViCriteria{}");
    }

    private static void setAllFilters(SaisanCogViCriteria saisanCogViCriteria) {
        saisanCogViCriteria.id();
        saisanCogViCriteria.key();
        saisanCogViCriteria.valueJason();
        saisanCogViCriteria.distinct();
    }

    private static Condition<SaisanCogViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKey()) &&
                condition.apply(criteria.getValueJason()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SaisanCogViCriteria> copyFiltersAre(SaisanCogViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getKey(), copy.getKey()) &&
                condition.apply(criteria.getValueJason(), copy.getValueJason()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
