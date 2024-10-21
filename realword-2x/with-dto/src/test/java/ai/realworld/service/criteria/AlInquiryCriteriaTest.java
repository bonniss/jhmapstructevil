package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlInquiryCriteriaTest {

    @Test
    void newAlInquiryCriteriaHasAllFiltersNullTest() {
        var alInquiryCriteria = new AlInquiryCriteria();
        assertThat(alInquiryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alInquiryCriteriaFluentMethodsCreatesFiltersTest() {
        var alInquiryCriteria = new AlInquiryCriteria();

        setAllFilters(alInquiryCriteria);

        assertThat(alInquiryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alInquiryCriteriaCopyCreatesNullFilterTest() {
        var alInquiryCriteria = new AlInquiryCriteria();
        var copy = alInquiryCriteria.copy();

        assertThat(alInquiryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alInquiryCriteria)
        );
    }

    @Test
    void alInquiryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alInquiryCriteria = new AlInquiryCriteria();
        setAllFilters(alInquiryCriteria);

        var copy = alInquiryCriteria.copy();

        assertThat(alInquiryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alInquiryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alInquiryCriteria = new AlInquiryCriteria();

        assertThat(alInquiryCriteria).hasToString("AlInquiryCriteria{}");
    }

    private static void setAllFilters(AlInquiryCriteria alInquiryCriteria) {
        alInquiryCriteria.id();
        alInquiryCriteria.title();
        alInquiryCriteria.body();
        alInquiryCriteria.sender();
        alInquiryCriteria.email();
        alInquiryCriteria.phone();
        alInquiryCriteria.contentJason();
        alInquiryCriteria.customerId();
        alInquiryCriteria.agencyId();
        alInquiryCriteria.personInChargeId();
        alInquiryCriteria.applicationId();
        alInquiryCriteria.distinct();
    }

    private static Condition<AlInquiryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlInquiryCriteria> copyFiltersAre(AlInquiryCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
