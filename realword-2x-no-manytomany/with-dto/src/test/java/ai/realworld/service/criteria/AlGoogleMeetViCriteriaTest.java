package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlGoogleMeetViCriteriaTest {

    @Test
    void newAlGoogleMeetViCriteriaHasAllFiltersNullTest() {
        var alGoogleMeetViCriteria = new AlGoogleMeetViCriteria();
        assertThat(alGoogleMeetViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alGoogleMeetViCriteriaFluentMethodsCreatesFiltersTest() {
        var alGoogleMeetViCriteria = new AlGoogleMeetViCriteria();

        setAllFilters(alGoogleMeetViCriteria);

        assertThat(alGoogleMeetViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alGoogleMeetViCriteriaCopyCreatesNullFilterTest() {
        var alGoogleMeetViCriteria = new AlGoogleMeetViCriteria();
        var copy = alGoogleMeetViCriteria.copy();

        assertThat(alGoogleMeetViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alGoogleMeetViCriteria)
        );
    }

    @Test
    void alGoogleMeetViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alGoogleMeetViCriteria = new AlGoogleMeetViCriteria();
        setAllFilters(alGoogleMeetViCriteria);

        var copy = alGoogleMeetViCriteria.copy();

        assertThat(alGoogleMeetViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alGoogleMeetViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alGoogleMeetViCriteria = new AlGoogleMeetViCriteria();

        assertThat(alGoogleMeetViCriteria).hasToString("AlGoogleMeetViCriteria{}");
    }

    private static void setAllFilters(AlGoogleMeetViCriteria alGoogleMeetViCriteria) {
        alGoogleMeetViCriteria.id();
        alGoogleMeetViCriteria.title();
        alGoogleMeetViCriteria.description();
        alGoogleMeetViCriteria.numberOfParticipants();
        alGoogleMeetViCriteria.plannedDate();
        alGoogleMeetViCriteria.plannedDurationInMinutes();
        alGoogleMeetViCriteria.actualDate();
        alGoogleMeetViCriteria.actualDurationInMinutes();
        alGoogleMeetViCriteria.contentJason();
        alGoogleMeetViCriteria.customerId();
        alGoogleMeetViCriteria.agencyId();
        alGoogleMeetViCriteria.personInChargeId();
        alGoogleMeetViCriteria.applicationId();
        alGoogleMeetViCriteria.distinct();
    }

    private static Condition<AlGoogleMeetViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlGoogleMeetViCriteria> copyFiltersAre(
        AlGoogleMeetViCriteria copy,
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
