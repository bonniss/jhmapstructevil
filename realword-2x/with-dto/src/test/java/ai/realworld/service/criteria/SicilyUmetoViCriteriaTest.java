package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SicilyUmetoViCriteriaTest {

    @Test
    void newSicilyUmetoViCriteriaHasAllFiltersNullTest() {
        var sicilyUmetoViCriteria = new SicilyUmetoViCriteria();
        assertThat(sicilyUmetoViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void sicilyUmetoViCriteriaFluentMethodsCreatesFiltersTest() {
        var sicilyUmetoViCriteria = new SicilyUmetoViCriteria();

        setAllFilters(sicilyUmetoViCriteria);

        assertThat(sicilyUmetoViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void sicilyUmetoViCriteriaCopyCreatesNullFilterTest() {
        var sicilyUmetoViCriteria = new SicilyUmetoViCriteria();
        var copy = sicilyUmetoViCriteria.copy();

        assertThat(sicilyUmetoViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(sicilyUmetoViCriteria)
        );
    }

    @Test
    void sicilyUmetoViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var sicilyUmetoViCriteria = new SicilyUmetoViCriteria();
        setAllFilters(sicilyUmetoViCriteria);

        var copy = sicilyUmetoViCriteria.copy();

        assertThat(sicilyUmetoViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(sicilyUmetoViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var sicilyUmetoViCriteria = new SicilyUmetoViCriteria();

        assertThat(sicilyUmetoViCriteria).hasToString("SicilyUmetoViCriteria{}");
    }

    private static void setAllFilters(SicilyUmetoViCriteria sicilyUmetoViCriteria) {
        sicilyUmetoViCriteria.id();
        sicilyUmetoViCriteria.type();
        sicilyUmetoViCriteria.content();
        sicilyUmetoViCriteria.distinct();
    }

    private static Condition<SicilyUmetoViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getContent()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SicilyUmetoViCriteria> copyFiltersAre(
        SicilyUmetoViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getContent(), copy.getContent()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
