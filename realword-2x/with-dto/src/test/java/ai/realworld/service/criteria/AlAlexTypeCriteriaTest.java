package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlAlexTypeCriteriaTest {

    @Test
    void newAlAlexTypeCriteriaHasAllFiltersNullTest() {
        var alAlexTypeCriteria = new AlAlexTypeCriteria();
        assertThat(alAlexTypeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alAlexTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var alAlexTypeCriteria = new AlAlexTypeCriteria();

        setAllFilters(alAlexTypeCriteria);

        assertThat(alAlexTypeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alAlexTypeCriteriaCopyCreatesNullFilterTest() {
        var alAlexTypeCriteria = new AlAlexTypeCriteria();
        var copy = alAlexTypeCriteria.copy();

        assertThat(alAlexTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alAlexTypeCriteria)
        );
    }

    @Test
    void alAlexTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alAlexTypeCriteria = new AlAlexTypeCriteria();
        setAllFilters(alAlexTypeCriteria);

        var copy = alAlexTypeCriteria.copy();

        assertThat(alAlexTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alAlexTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alAlexTypeCriteria = new AlAlexTypeCriteria();

        assertThat(alAlexTypeCriteria).hasToString("AlAlexTypeCriteria{}");
    }

    private static void setAllFilters(AlAlexTypeCriteria alAlexTypeCriteria) {
        alAlexTypeCriteria.id();
        alAlexTypeCriteria.name();
        alAlexTypeCriteria.description();
        alAlexTypeCriteria.canDoRetail();
        alAlexTypeCriteria.isOrgDivision();
        alAlexTypeCriteria.configJason();
        alAlexTypeCriteria.treeDepth();
        alAlexTypeCriteria.applicationId();
        alAlexTypeCriteria.asSupplierId();
        alAlexTypeCriteria.asCustomerId();
        alAlexTypeCriteria.agenciesId();
        alAlexTypeCriteria.distinct();
    }

    private static Condition<AlAlexTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlAlexTypeCriteria> copyFiltersAre(AlAlexTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
