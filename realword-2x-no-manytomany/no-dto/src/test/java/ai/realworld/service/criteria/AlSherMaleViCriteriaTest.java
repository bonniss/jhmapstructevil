package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlSherMaleViCriteriaTest {

    @Test
    void newAlSherMaleViCriteriaHasAllFiltersNullTest() {
        var alSherMaleViCriteria = new AlSherMaleViCriteria();
        assertThat(alSherMaleViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alSherMaleViCriteriaFluentMethodsCreatesFiltersTest() {
        var alSherMaleViCriteria = new AlSherMaleViCriteria();

        setAllFilters(alSherMaleViCriteria);

        assertThat(alSherMaleViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alSherMaleViCriteriaCopyCreatesNullFilterTest() {
        var alSherMaleViCriteria = new AlSherMaleViCriteria();
        var copy = alSherMaleViCriteria.copy();

        assertThat(alSherMaleViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alSherMaleViCriteria)
        );
    }

    @Test
    void alSherMaleViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alSherMaleViCriteria = new AlSherMaleViCriteria();
        setAllFilters(alSherMaleViCriteria);

        var copy = alSherMaleViCriteria.copy();

        assertThat(alSherMaleViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alSherMaleViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alSherMaleViCriteria = new AlSherMaleViCriteria();

        assertThat(alSherMaleViCriteria).hasToString("AlSherMaleViCriteria{}");
    }

    private static void setAllFilters(AlSherMaleViCriteria alSherMaleViCriteria) {
        alSherMaleViCriteria.id();
        alSherMaleViCriteria.dataSourceMappingType();
        alSherMaleViCriteria.keyword();
        alSherMaleViCriteria.property();
        alSherMaleViCriteria.title();
        alSherMaleViCriteria.applicationId();
        alSherMaleViCriteria.distinct();
    }

    private static Condition<AlSherMaleViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataSourceMappingType()) &&
                condition.apply(criteria.getKeyword()) &&
                condition.apply(criteria.getProperty()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlSherMaleViCriteria> copyFiltersAre(
        AlSherMaleViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataSourceMappingType(), copy.getDataSourceMappingType()) &&
                condition.apply(criteria.getKeyword(), copy.getKeyword()) &&
                condition.apply(criteria.getProperty(), copy.getProperty()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
