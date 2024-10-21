package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlProProViCriteriaTest {

    @Test
    void newAlProProViCriteriaHasAllFiltersNullTest() {
        var alProProViCriteria = new AlProProViCriteria();
        assertThat(alProProViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alProProViCriteriaFluentMethodsCreatesFiltersTest() {
        var alProProViCriteria = new AlProProViCriteria();

        setAllFilters(alProProViCriteria);

        assertThat(alProProViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alProProViCriteriaCopyCreatesNullFilterTest() {
        var alProProViCriteria = new AlProProViCriteria();
        var copy = alProProViCriteria.copy();

        assertThat(alProProViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alProProViCriteria)
        );
    }

    @Test
    void alProProViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alProProViCriteria = new AlProProViCriteria();
        setAllFilters(alProProViCriteria);

        var copy = alProProViCriteria.copy();

        assertThat(alProProViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alProProViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alProProViCriteria = new AlProProViCriteria();

        assertThat(alProProViCriteria).hasToString("AlProProViCriteria{}");
    }

    private static void setAllFilters(AlProProViCriteria alProProViCriteria) {
        alProProViCriteria.id();
        alProProViCriteria.name();
        alProProViCriteria.descriptionHeitiga();
        alProProViCriteria.propertyType();
        alProProViCriteria.areaInSquareMeter();
        alProProViCriteria.numberOfAdults();
        alProProViCriteria.numberOfPreschoolers();
        alProProViCriteria.numberOfChildren();
        alProProViCriteria.numberOfRooms();
        alProProViCriteria.numberOfFloors();
        alProProViCriteria.bedSize();
        alProProViCriteria.isEnabled();
        alProProViCriteria.parentId();
        alProProViCriteria.projectId();
        alProProViCriteria.avatarId();
        alProProViCriteria.applicationId();
        alProProViCriteria.childrenId();
        alProProViCriteria.distinct();
    }

    private static Condition<AlProProViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescriptionHeitiga()) &&
                condition.apply(criteria.getPropertyType()) &&
                condition.apply(criteria.getAreaInSquareMeter()) &&
                condition.apply(criteria.getNumberOfAdults()) &&
                condition.apply(criteria.getNumberOfPreschoolers()) &&
                condition.apply(criteria.getNumberOfChildren()) &&
                condition.apply(criteria.getNumberOfRooms()) &&
                condition.apply(criteria.getNumberOfFloors()) &&
                condition.apply(criteria.getBedSize()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getProjectId()) &&
                condition.apply(criteria.getAvatarId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlProProViCriteria> copyFiltersAre(AlProProViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescriptionHeitiga(), copy.getDescriptionHeitiga()) &&
                condition.apply(criteria.getPropertyType(), copy.getPropertyType()) &&
                condition.apply(criteria.getAreaInSquareMeter(), copy.getAreaInSquareMeter()) &&
                condition.apply(criteria.getNumberOfAdults(), copy.getNumberOfAdults()) &&
                condition.apply(criteria.getNumberOfPreschoolers(), copy.getNumberOfPreschoolers()) &&
                condition.apply(criteria.getNumberOfChildren(), copy.getNumberOfChildren()) &&
                condition.apply(criteria.getNumberOfRooms(), copy.getNumberOfRooms()) &&
                condition.apply(criteria.getNumberOfFloors(), copy.getNumberOfFloors()) &&
                condition.apply(criteria.getBedSize(), copy.getBedSize()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getProjectId(), copy.getProjectId()) &&
                condition.apply(criteria.getAvatarId(), copy.getAvatarId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
