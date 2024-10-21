package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlInquiryViCriteriaTest {

    @Test
    void newAlInquiryViCriteriaHasAllFiltersNullTest() {
        var alInquiryViCriteria = new AlInquiryViCriteria();
        assertThat(alInquiryViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alInquiryViCriteriaFluentMethodsCreatesFiltersTest() {
        var alInquiryViCriteria = new AlInquiryViCriteria();

        setAllFilters(alInquiryViCriteria);

        assertThat(alInquiryViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alInquiryViCriteriaCopyCreatesNullFilterTest() {
        var alInquiryViCriteria = new AlInquiryViCriteria();
        var copy = alInquiryViCriteria.copy();

        assertThat(alInquiryViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alInquiryViCriteria)
        );
    }

    @Test
    void alInquiryViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alInquiryViCriteria = new AlInquiryViCriteria();
        setAllFilters(alInquiryViCriteria);

        var copy = alInquiryViCriteria.copy();

        assertThat(alInquiryViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alInquiryViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alInquiryViCriteria = new AlInquiryViCriteria();

        assertThat(alInquiryViCriteria).hasToString("AlInquiryViCriteria{}");
    }

    private static void setAllFilters(AlInquiryViCriteria alInquiryViCriteria) {
        alInquiryViCriteria.id();
        alInquiryViCriteria.title();
        alInquiryViCriteria.body();
        alInquiryViCriteria.sender();
        alInquiryViCriteria.email();
        alInquiryViCriteria.phone();
        alInquiryViCriteria.contentJason();
        alInquiryViCriteria.customerId();
        alInquiryViCriteria.agencyId();
        alInquiryViCriteria.personInChargeId();
        alInquiryViCriteria.applicationId();
        alInquiryViCriteria.distinct();
    }

    private static Condition<AlInquiryViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getBody()) &&
                condition.apply(criteria.getSender()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getContentJason()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getAgencyId()) &&
                condition.apply(criteria.getPersonInChargeId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlInquiryViCriteria> copyFiltersAre(AlInquiryViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getBody(), copy.getBody()) &&
                condition.apply(criteria.getSender(), copy.getSender()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
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
