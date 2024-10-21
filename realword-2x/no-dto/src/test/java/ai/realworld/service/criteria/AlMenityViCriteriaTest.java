package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlMenityViCriteriaTest {

    @Test
    void newAlMenityViCriteriaHasAllFiltersNullTest() {
        var alMenityViCriteria = new AlMenityViCriteria();
        assertThat(alMenityViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alMenityViCriteriaFluentMethodsCreatesFiltersTest() {
        var alMenityViCriteria = new AlMenityViCriteria();

        setAllFilters(alMenityViCriteria);

        assertThat(alMenityViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alMenityViCriteriaCopyCreatesNullFilterTest() {
        var alMenityViCriteria = new AlMenityViCriteria();
        var copy = alMenityViCriteria.copy();

        assertThat(alMenityViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alMenityViCriteria)
        );
    }

    @Test
    void alMenityViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alMenityViCriteria = new AlMenityViCriteria();
        setAllFilters(alMenityViCriteria);

        var copy = alMenityViCriteria.copy();

        assertThat(alMenityViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alMenityViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alMenityViCriteria = new AlMenityViCriteria();

        assertThat(alMenityViCriteria).hasToString("AlMenityViCriteria{}");
    }

    private static void setAllFilters(AlMenityViCriteria alMenityViCriteria) {
        alMenityViCriteria.id();
        alMenityViCriteria.name();
        alMenityViCriteria.iconSvg();
        alMenityViCriteria.propertyType();
        alMenityViCriteria.applicationId();
        alMenityViCriteria.propertyProfileId();
        alMenityViCriteria.distinct();
    }

    private static Condition<AlMenityViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIconSvg()) &&
                condition.apply(criteria.getPropertyType()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getPropertyProfileId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlMenityViCriteria> copyFiltersAre(AlMenityViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIconSvg(), copy.getIconSvg()) &&
                condition.apply(criteria.getPropertyType(), copy.getPropertyType()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getPropertyProfileId(), copy.getPropertyProfileId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
