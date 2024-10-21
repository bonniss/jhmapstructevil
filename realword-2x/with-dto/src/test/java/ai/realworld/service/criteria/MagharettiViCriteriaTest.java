package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MagharettiViCriteriaTest {

    @Test
    void newMagharettiViCriteriaHasAllFiltersNullTest() {
        var magharettiViCriteria = new MagharettiViCriteria();
        assertThat(magharettiViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void magharettiViCriteriaFluentMethodsCreatesFiltersTest() {
        var magharettiViCriteria = new MagharettiViCriteria();

        setAllFilters(magharettiViCriteria);

        assertThat(magharettiViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void magharettiViCriteriaCopyCreatesNullFilterTest() {
        var magharettiViCriteria = new MagharettiViCriteria();
        var copy = magharettiViCriteria.copy();

        assertThat(magharettiViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(magharettiViCriteria)
        );
    }

    @Test
    void magharettiViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var magharettiViCriteria = new MagharettiViCriteria();
        setAllFilters(magharettiViCriteria);

        var copy = magharettiViCriteria.copy();

        assertThat(magharettiViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(magharettiViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var magharettiViCriteria = new MagharettiViCriteria();

        assertThat(magharettiViCriteria).hasToString("MagharettiViCriteria{}");
    }

    private static void setAllFilters(MagharettiViCriteria magharettiViCriteria) {
        magharettiViCriteria.id();
        magharettiViCriteria.name();
        magharettiViCriteria.label();
        magharettiViCriteria.type();
        magharettiViCriteria.distinct();
    }

    private static Condition<MagharettiViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<MagharettiViCriteria> copyFiltersAre(
        MagharettiViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
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
