package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SicilyUmetoCriteriaTest {

    @Test
    void newSicilyUmetoCriteriaHasAllFiltersNullTest() {
        var sicilyUmetoCriteria = new SicilyUmetoCriteria();
        assertThat(sicilyUmetoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void sicilyUmetoCriteriaFluentMethodsCreatesFiltersTest() {
        var sicilyUmetoCriteria = new SicilyUmetoCriteria();

        setAllFilters(sicilyUmetoCriteria);

        assertThat(sicilyUmetoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void sicilyUmetoCriteriaCopyCreatesNullFilterTest() {
        var sicilyUmetoCriteria = new SicilyUmetoCriteria();
        var copy = sicilyUmetoCriteria.copy();

        assertThat(sicilyUmetoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(sicilyUmetoCriteria)
        );
    }

    @Test
    void sicilyUmetoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var sicilyUmetoCriteria = new SicilyUmetoCriteria();
        setAllFilters(sicilyUmetoCriteria);

        var copy = sicilyUmetoCriteria.copy();

        assertThat(sicilyUmetoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(sicilyUmetoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var sicilyUmetoCriteria = new SicilyUmetoCriteria();

        assertThat(sicilyUmetoCriteria).hasToString("SicilyUmetoCriteria{}");
    }

    private static void setAllFilters(SicilyUmetoCriteria sicilyUmetoCriteria) {
        sicilyUmetoCriteria.id();
        sicilyUmetoCriteria.type();
        sicilyUmetoCriteria.content();
        sicilyUmetoCriteria.distinct();
    }

    private static Condition<SicilyUmetoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getContent()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SicilyUmetoCriteria> copyFiltersAre(SicilyUmetoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
