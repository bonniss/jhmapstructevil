package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlAlexTypeViCriteriaTest {

    @Test
    void newAlAlexTypeViCriteriaHasAllFiltersNullTest() {
        var alAlexTypeViCriteria = new AlAlexTypeViCriteria();
        assertThat(alAlexTypeViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alAlexTypeViCriteriaFluentMethodsCreatesFiltersTest() {
        var alAlexTypeViCriteria = new AlAlexTypeViCriteria();

        setAllFilters(alAlexTypeViCriteria);

        assertThat(alAlexTypeViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alAlexTypeViCriteriaCopyCreatesNullFilterTest() {
        var alAlexTypeViCriteria = new AlAlexTypeViCriteria();
        var copy = alAlexTypeViCriteria.copy();

        assertThat(alAlexTypeViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alAlexTypeViCriteria)
        );
    }

    @Test
    void alAlexTypeViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alAlexTypeViCriteria = new AlAlexTypeViCriteria();
        setAllFilters(alAlexTypeViCriteria);

        var copy = alAlexTypeViCriteria.copy();

        assertThat(alAlexTypeViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alAlexTypeViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alAlexTypeViCriteria = new AlAlexTypeViCriteria();

        assertThat(alAlexTypeViCriteria).hasToString("AlAlexTypeViCriteria{}");
    }

    private static void setAllFilters(AlAlexTypeViCriteria alAlexTypeViCriteria) {
        alAlexTypeViCriteria.id();
        alAlexTypeViCriteria.name();
        alAlexTypeViCriteria.description();
        alAlexTypeViCriteria.canDoRetail();
        alAlexTypeViCriteria.isOrgDivision();
        alAlexTypeViCriteria.configJason();
        alAlexTypeViCriteria.treeDepth();
        alAlexTypeViCriteria.applicationId();
        alAlexTypeViCriteria.asSupplierId();
        alAlexTypeViCriteria.asCustomerId();
        alAlexTypeViCriteria.agenciesId();
        alAlexTypeViCriteria.distinct();
    }

    private static Condition<AlAlexTypeViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCanDoRetail()) &&
                condition.apply(criteria.getIsOrgDivision()) &&
                condition.apply(criteria.getConfigJason()) &&
                condition.apply(criteria.getTreeDepth()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getAsSupplierId()) &&
                condition.apply(criteria.getAsCustomerId()) &&
                condition.apply(criteria.getAgenciesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlAlexTypeViCriteria> copyFiltersAre(
        AlAlexTypeViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCanDoRetail(), copy.getCanDoRetail()) &&
                condition.apply(criteria.getIsOrgDivision(), copy.getIsOrgDivision()) &&
                condition.apply(criteria.getConfigJason(), copy.getConfigJason()) &&
                condition.apply(criteria.getTreeDepth(), copy.getTreeDepth()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getAsSupplierId(), copy.getAsSupplierId()) &&
                condition.apply(criteria.getAsCustomerId(), copy.getAsCustomerId()) &&
                condition.apply(criteria.getAgenciesId(), copy.getAgenciesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
