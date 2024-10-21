package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlGoogleMeetCriteriaTest {

    @Test
    void newAlGoogleMeetCriteriaHasAllFiltersNullTest() {
        var alGoogleMeetCriteria = new AlGoogleMeetCriteria();
        assertThat(alGoogleMeetCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alGoogleMeetCriteriaFluentMethodsCreatesFiltersTest() {
        var alGoogleMeetCriteria = new AlGoogleMeetCriteria();

        setAllFilters(alGoogleMeetCriteria);

        assertThat(alGoogleMeetCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alGoogleMeetCriteriaCopyCreatesNullFilterTest() {
        var alGoogleMeetCriteria = new AlGoogleMeetCriteria();
        var copy = alGoogleMeetCriteria.copy();

        assertThat(alGoogleMeetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alGoogleMeetCriteria)
        );
    }

    @Test
    void alGoogleMeetCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alGoogleMeetCriteria = new AlGoogleMeetCriteria();
        setAllFilters(alGoogleMeetCriteria);

        var copy = alGoogleMeetCriteria.copy();

        assertThat(alGoogleMeetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alGoogleMeetCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alGoogleMeetCriteria = new AlGoogleMeetCriteria();

        assertThat(alGoogleMeetCriteria).hasToString("AlGoogleMeetCriteria{}");
    }

    private static void setAllFilters(AlGoogleMeetCriteria alGoogleMeetCriteria) {
        alGoogleMeetCriteria.id();
        alGoogleMeetCriteria.title();
        alGoogleMeetCriteria.description();
        alGoogleMeetCriteria.numberOfParticipants();
        alGoogleMeetCriteria.plannedDate();
        alGoogleMeetCriteria.plannedDurationInMinutes();
        alGoogleMeetCriteria.actualDate();
        alGoogleMeetCriteria.actualDurationInMinutes();
        alGoogleMeetCriteria.contentJason();
        alGoogleMeetCriteria.customerId();
        alGoogleMeetCriteria.agencyId();
        alGoogleMeetCriteria.personInChargeId();
        alGoogleMeetCriteria.applicationId();
        alGoogleMeetCriteria.distinct();
    }

    private static Condition<AlGoogleMeetCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getNumberOfParticipants()) &&
                condition.apply(criteria.getPlannedDate()) &&
                condition.apply(criteria.getPlannedDurationInMinutes()) &&
                condition.apply(criteria.getActualDate()) &&
                condition.apply(criteria.getActualDurationInMinutes()) &&
                condition.apply(criteria.getContentJason()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getAgencyId()) &&
                condition.apply(criteria.getPersonInChargeId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlGoogleMeetCriteria> copyFiltersAre(
        AlGoogleMeetCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getNumberOfParticipants(), copy.getNumberOfParticipants()) &&
                condition.apply(criteria.getPlannedDate(), copy.getPlannedDate()) &&
                condition.apply(criteria.getPlannedDurationInMinutes(), copy.getPlannedDurationInMinutes()) &&
                condition.apply(criteria.getActualDate(), copy.getActualDate()) &&
                condition.apply(criteria.getActualDurationInMinutes(), copy.getActualDurationInMinutes()) &&
                condition.apply(criteria.getContentJason(), copy.getContentJason()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getAgencyId(), copy.getAgencyId()) &&
                condition.apply(criteria.getPersonInChargeId(), copy.getPersonInChargeId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
