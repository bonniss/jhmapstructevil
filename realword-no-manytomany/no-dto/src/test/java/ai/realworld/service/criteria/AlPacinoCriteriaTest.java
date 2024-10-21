package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPacinoCriteriaTest {

    @Test
    void newAlPacinoCriteriaHasAllFiltersNullTest() {
        var alPacinoCriteria = new AlPacinoCriteria();
        assertThat(alPacinoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPacinoCriteriaFluentMethodsCreatesFiltersTest() {
        var alPacinoCriteria = new AlPacinoCriteria();

        setAllFilters(alPacinoCriteria);

        assertThat(alPacinoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPacinoCriteriaCopyCreatesNullFilterTest() {
        var alPacinoCriteria = new AlPacinoCriteria();
        var copy = alPacinoCriteria.copy();

        assertThat(alPacinoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoCriteria)
        );
    }

    @Test
    void alPacinoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPacinoCriteria = new AlPacinoCriteria();
        setAllFilters(alPacinoCriteria);

        var copy = alPacinoCriteria.copy();

        assertThat(alPacinoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPacinoCriteria = new AlPacinoCriteria();

        assertThat(alPacinoCriteria).hasToString("AlPacinoCriteria{}");
    }

    private static void setAllFilters(AlPacinoCriteria alPacinoCriteria) {
        alPacinoCriteria.id();
        alPacinoCriteria.platformCode();
        alPacinoCriteria.platformUsername();
        alPacinoCriteria.platformAvatarUrl();
        alPacinoCriteria.isSensitive();
        alPacinoCriteria.familyName();
        alPacinoCriteria.givenName();
        alPacinoCriteria.dob();
        alPacinoCriteria.gender();
        alPacinoCriteria.phone();
        alPacinoCriteria.email();
        alPacinoCriteria.acquiredFrom();
        alPacinoCriteria.currentPoints();
        alPacinoCriteria.totalPoints();
        alPacinoCriteria.isFollowing();
        alPacinoCriteria.isEnabled();
        alPacinoCriteria.applicationId();
        alPacinoCriteria.membershipTierId();
        alPacinoCriteria.alVueVueUsageId();
        alPacinoCriteria.distinct();
    }

    private static Condition<AlPacinoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPlatformCode()) &&
                condition.apply(criteria.getPlatformUsername()) &&
                condition.apply(criteria.getPlatformAvatarUrl()) &&
                condition.apply(criteria.getIsSensitive()) &&
                condition.apply(criteria.getFamilyName()) &&
                condition.apply(criteria.getGivenName()) &&
                condition.apply(criteria.getDob()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getAcquiredFrom()) &&
                condition.apply(criteria.getCurrentPoints()) &&
                condition.apply(criteria.getTotalPoints()) &&
                condition.apply(criteria.getIsFollowing()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getMembershipTierId()) &&
                condition.apply(criteria.getAlVueVueUsageId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlPacinoCriteria> copyFiltersAre(AlPacinoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPlatformCode(), copy.getPlatformCode()) &&
                condition.apply(criteria.getPlatformUsername(), copy.getPlatformUsername()) &&
                condition.apply(criteria.getPlatformAvatarUrl(), copy.getPlatformAvatarUrl()) &&
                condition.apply(criteria.getIsSensitive(), copy.getIsSensitive()) &&
                condition.apply(criteria.getFamilyName(), copy.getFamilyName()) &&
                condition.apply(criteria.getGivenName(), copy.getGivenName()) &&
                condition.apply(criteria.getDob(), copy.getDob()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getAcquiredFrom(), copy.getAcquiredFrom()) &&
                condition.apply(criteria.getCurrentPoints(), copy.getCurrentPoints()) &&
                condition.apply(criteria.getTotalPoints(), copy.getTotalPoints()) &&
                condition.apply(criteria.getIsFollowing(), copy.getIsFollowing()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getMembershipTierId(), copy.getMembershipTierId()) &&
                condition.apply(criteria.getAlVueVueUsageId(), copy.getAlVueVueUsageId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
