package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MagharettiCriteriaTest {

    @Test
    void newMagharettiCriteriaHasAllFiltersNullTest() {
        var magharettiCriteria = new MagharettiCriteria();
        assertThat(magharettiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void magharettiCriteriaFluentMethodsCreatesFiltersTest() {
        var magharettiCriteria = new MagharettiCriteria();

        setAllFilters(magharettiCriteria);

        assertThat(magharettiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void magharettiCriteriaCopyCreatesNullFilterTest() {
        var magharettiCriteria = new MagharettiCriteria();
        var copy = magharettiCriteria.copy();

        assertThat(magharettiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(magharettiCriteria)
        );
    }

    @Test
    void magharettiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var magharettiCriteria = new MagharettiCriteria();
        setAllFilters(magharettiCriteria);

        var copy = magharettiCriteria.copy();

        assertThat(magharettiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(magharettiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var magharettiCriteria = new MagharettiCriteria();

        assertThat(magharettiCriteria).hasToString("MagharettiCriteria{}");
    }

    private static void setAllFilters(MagharettiCriteria magharettiCriteria) {
        magharettiCriteria.id();
        magharettiCriteria.name();
        magharettiCriteria.label();
        magharettiCriteria.type();
        magharettiCriteria.distinct();
    }

    private static Condition<MagharettiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getLabel()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MagharettiCriteria> copyFiltersAre(MagharettiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getLabel(), copy.getLabel()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
