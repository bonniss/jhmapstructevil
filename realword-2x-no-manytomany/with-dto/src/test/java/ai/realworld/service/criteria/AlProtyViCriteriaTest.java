package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlProtyViCriteriaTest {

    @Test
    void newAlProtyViCriteriaHasAllFiltersNullTest() {
        var alProtyViCriteria = new AlProtyViCriteria();
        assertThat(alProtyViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alProtyViCriteriaFluentMethodsCreatesFiltersTest() {
        var alProtyViCriteria = new AlProtyViCriteria();

        setAllFilters(alProtyViCriteria);

        assertThat(alProtyViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alProtyViCriteriaCopyCreatesNullFilterTest() {
        var alProtyViCriteria = new AlProtyViCriteria();
        var copy = alProtyViCriteria.copy();

        assertThat(alProtyViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alProtyViCriteria)
        );
    }

    @Test
    void alProtyViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alProtyViCriteria = new AlProtyViCriteria();
        setAllFilters(alProtyViCriteria);

        var copy = alProtyViCriteria.copy();

        assertThat(alProtyViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alProtyViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alProtyViCriteria = new AlProtyViCriteria();

        assertThat(alProtyViCriteria).hasToString("AlProtyViCriteria{}");
    }

    private static void setAllFilters(AlProtyViCriteria alProtyViCriteria) {
        alProtyViCriteria.id();
        alProtyViCriteria.name();
        alProtyViCriteria.descriptionHeitiga();
        alProtyViCriteria.coordinate();
        alProtyViCriteria.code();
        alProtyViCriteria.status();
        alProtyViCriteria.isEnabled();
        alProtyViCriteria.parentId();
        alProtyViCriteria.operatorId();
        alProtyViCriteria.propertyProfileId();
        alProtyViCriteria.avatarId();
        alProtyViCriteria.applicationId();
        alProtyViCriteria.childrenId();
        alProtyViCriteria.distinct();
    }

    private static Condition<AlProtyViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescriptionHeitiga()) &&
                condition.apply(criteria.getCoordinate()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getOperatorId()) &&
                condition.apply(criteria.getPropertyProfileId()) &&
                condition.apply(criteria.getAvatarId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlProtyViCriteria> copyFiltersAre(AlProtyViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescriptionHeitiga(), copy.getDescriptionHeitiga()) &&
                condition.apply(criteria.getCoordinate(), copy.getCoordinate()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getOperatorId(), copy.getOperatorId()) &&
                condition.apply(criteria.getPropertyProfileId(), copy.getPropertyProfileId()) &&
                condition.apply(criteria.getAvatarId(), copy.getAvatarId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
