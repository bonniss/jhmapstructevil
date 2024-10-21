package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SaisanCogCriteriaTest {

    @Test
    void newSaisanCogCriteriaHasAllFiltersNullTest() {
        var saisanCogCriteria = new SaisanCogCriteria();
        assertThat(saisanCogCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void saisanCogCriteriaFluentMethodsCreatesFiltersTest() {
        var saisanCogCriteria = new SaisanCogCriteria();

        setAllFilters(saisanCogCriteria);

        assertThat(saisanCogCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void saisanCogCriteriaCopyCreatesNullFilterTest() {
        var saisanCogCriteria = new SaisanCogCriteria();
        var copy = saisanCogCriteria.copy();

        assertThat(saisanCogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(saisanCogCriteria)
        );
    }

    @Test
    void saisanCogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var saisanCogCriteria = new SaisanCogCriteria();
        setAllFilters(saisanCogCriteria);

        var copy = saisanCogCriteria.copy();

        assertThat(saisanCogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(saisanCogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var saisanCogCriteria = new SaisanCogCriteria();

        assertThat(saisanCogCriteria).hasToString("SaisanCogCriteria{}");
    }

    private static void setAllFilters(SaisanCogCriteria saisanCogCriteria) {
        saisanCogCriteria.id();
        saisanCogCriteria.key();
        saisanCogCriteria.valueJason();
        saisanCogCriteria.distinct();
    }

    private static Condition<SaisanCogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKey()) &&
                condition.apply(criteria.getValueJason()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SaisanCogCriteria> copyFiltersAre(SaisanCogCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
