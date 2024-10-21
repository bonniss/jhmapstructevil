package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlSherMaleCriteriaTest {

    @Test
    void newAlSherMaleCriteriaHasAllFiltersNullTest() {
        var alSherMaleCriteria = new AlSherMaleCriteria();
        assertThat(alSherMaleCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alSherMaleCriteriaFluentMethodsCreatesFiltersTest() {
        var alSherMaleCriteria = new AlSherMaleCriteria();

        setAllFilters(alSherMaleCriteria);

        assertThat(alSherMaleCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alSherMaleCriteriaCopyCreatesNullFilterTest() {
        var alSherMaleCriteria = new AlSherMaleCriteria();
        var copy = alSherMaleCriteria.copy();

        assertThat(alSherMaleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alSherMaleCriteria)
        );
    }

    @Test
    void alSherMaleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alSherMaleCriteria = new AlSherMaleCriteria();
        setAllFilters(alSherMaleCriteria);

        var copy = alSherMaleCriteria.copy();

        assertThat(alSherMaleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alSherMaleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alSherMaleCriteria = new AlSherMaleCriteria();

        assertThat(alSherMaleCriteria).hasToString("AlSherMaleCriteria{}");
    }

    private static void setAllFilters(AlSherMaleCriteria alSherMaleCriteria) {
        alSherMaleCriteria.id();
        alSherMaleCriteria.dataSourceMappingType();
        alSherMaleCriteria.keyword();
        alSherMaleCriteria.property();
        alSherMaleCriteria.title();
        alSherMaleCriteria.applicationId();
        alSherMaleCriteria.distinct();
    }

    private static Condition<AlSherMaleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlSherMaleCriteria> copyFiltersAre(AlSherMaleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
