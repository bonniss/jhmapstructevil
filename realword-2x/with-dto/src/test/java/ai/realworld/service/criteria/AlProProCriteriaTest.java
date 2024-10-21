package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlProProCriteriaTest {

    @Test
    void newAlProProCriteriaHasAllFiltersNullTest() {
        var alProProCriteria = new AlProProCriteria();
        assertThat(alProProCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alProProCriteriaFluentMethodsCreatesFiltersTest() {
        var alProProCriteria = new AlProProCriteria();

        setAllFilters(alProProCriteria);

        assertThat(alProProCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alProProCriteriaCopyCreatesNullFilterTest() {
        var alProProCriteria = new AlProProCriteria();
        var copy = alProProCriteria.copy();

        assertThat(alProProCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alProProCriteria)
        );
    }

    @Test
    void alProProCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alProProCriteria = new AlProProCriteria();
        setAllFilters(alProProCriteria);

        var copy = alProProCriteria.copy();

        assertThat(alProProCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alProProCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alProProCriteria = new AlProProCriteria();

        assertThat(alProProCriteria).hasToString("AlProProCriteria{}");
    }

    private static void setAllFilters(AlProProCriteria alProProCriteria) {
        alProProCriteria.id();
        alProProCriteria.name();
        alProProCriteria.descriptionHeitiga();
        alProProCriteria.propertyType();
        alProProCriteria.areaInSquareMeter();
        alProProCriteria.numberOfAdults();
        alProProCriteria.numberOfPreschoolers();
        alProProCriteria.numberOfChildren();
        alProProCriteria.numberOfRooms();
        alProProCriteria.numberOfFloors();
        alProProCriteria.bedSize();
        alProProCriteria.isEnabled();
        alProProCriteria.parentId();
        alProProCriteria.projectId();
        alProProCriteria.avatarId();
        alProProCriteria.applicationId();
        alProProCriteria.amenityId();
        alProProCriteria.imageId();
        alProProCriteria.childrenId();
        alProProCriteria.distinct();
    }

    private static Condition<AlProProCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getAmenityId()) &&
                condition.apply(criteria.getImageId()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlProProCriteria> copyFiltersAre(AlProProCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
                condition.apply(criteria.getAmenityId(), copy.getAmenityId()) &&
                condition.apply(criteria.getImageId(), copy.getImageId()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
