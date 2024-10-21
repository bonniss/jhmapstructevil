package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlProtyCriteriaTest {

    @Test
    void newAlProtyCriteriaHasAllFiltersNullTest() {
        var alProtyCriteria = new AlProtyCriteria();
        assertThat(alProtyCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alProtyCriteriaFluentMethodsCreatesFiltersTest() {
        var alProtyCriteria = new AlProtyCriteria();

        setAllFilters(alProtyCriteria);

        assertThat(alProtyCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alProtyCriteriaCopyCreatesNullFilterTest() {
        var alProtyCriteria = new AlProtyCriteria();
        var copy = alProtyCriteria.copy();

        assertThat(alProtyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alProtyCriteria)
        );
    }

    @Test
    void alProtyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alProtyCriteria = new AlProtyCriteria();
        setAllFilters(alProtyCriteria);

        var copy = alProtyCriteria.copy();

        assertThat(alProtyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alProtyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alProtyCriteria = new AlProtyCriteria();

        assertThat(alProtyCriteria).hasToString("AlProtyCriteria{}");
    }

    private static void setAllFilters(AlProtyCriteria alProtyCriteria) {
        alProtyCriteria.id();
        alProtyCriteria.name();
        alProtyCriteria.descriptionHeitiga();
        alProtyCriteria.coordinate();
        alProtyCriteria.code();
        alProtyCriteria.status();
        alProtyCriteria.isEnabled();
        alProtyCriteria.parentId();
        alProtyCriteria.operatorId();
        alProtyCriteria.propertyProfileId();
        alProtyCriteria.avatarId();
        alProtyCriteria.applicationId();
        alProtyCriteria.imageId();
        alProtyCriteria.childrenId();
        alProtyCriteria.bookingId();
        alProtyCriteria.distinct();
    }

    private static Condition<AlProtyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getImageId()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getBookingId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlProtyCriteria> copyFiltersAre(AlProtyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
                condition.apply(criteria.getImageId(), copy.getImageId()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getBookingId(), copy.getBookingId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
