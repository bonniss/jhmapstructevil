package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EdSheeranCriteriaTest {

    @Test
    void newEdSheeranCriteriaHasAllFiltersNullTest() {
        var edSheeranCriteria = new EdSheeranCriteria();
        assertThat(edSheeranCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void edSheeranCriteriaFluentMethodsCreatesFiltersTest() {
        var edSheeranCriteria = new EdSheeranCriteria();

        setAllFilters(edSheeranCriteria);

        assertThat(edSheeranCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void edSheeranCriteriaCopyCreatesNullFilterTest() {
        var edSheeranCriteria = new EdSheeranCriteria();
        var copy = edSheeranCriteria.copy();

        assertThat(edSheeranCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(edSheeranCriteria)
        );
    }

    @Test
    void edSheeranCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var edSheeranCriteria = new EdSheeranCriteria();
        setAllFilters(edSheeranCriteria);

        var copy = edSheeranCriteria.copy();

        assertThat(edSheeranCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(edSheeranCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var edSheeranCriteria = new EdSheeranCriteria();

        assertThat(edSheeranCriteria).hasToString("EdSheeranCriteria{}");
    }

    private static void setAllFilters(EdSheeranCriteria edSheeranCriteria) {
        edSheeranCriteria.id();
        edSheeranCriteria.familyName();
        edSheeranCriteria.givenName();
        edSheeranCriteria.display();
        edSheeranCriteria.dob();
        edSheeranCriteria.gender();
        edSheeranCriteria.phone();
        edSheeranCriteria.contactsJason();
        edSheeranCriteria.isEnabled();
        edSheeranCriteria.agencyId();
        edSheeranCriteria.avatarId();
        edSheeranCriteria.internalUserId();
        edSheeranCriteria.appUserId();
        edSheeranCriteria.applicationId();
        edSheeranCriteria.agentRolesId();
        edSheeranCriteria.distinct();
    }

    private static Condition<EdSheeranCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFamilyName()) &&
                condition.apply(criteria.getGivenName()) &&
                condition.apply(criteria.getDisplay()) &&
                condition.apply(criteria.getDob()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getContactsJason()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getAgencyId()) &&
                condition.apply(criteria.getAvatarId()) &&
                condition.apply(criteria.getInternalUserId()) &&
                condition.apply(criteria.getAppUserId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getAgentRolesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EdSheeranCriteria> copyFiltersAre(EdSheeranCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFamilyName(), copy.getFamilyName()) &&
                condition.apply(criteria.getGivenName(), copy.getGivenName()) &&
                condition.apply(criteria.getDisplay(), copy.getDisplay()) &&
                condition.apply(criteria.getDob(), copy.getDob()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getContactsJason(), copy.getContactsJason()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getAgencyId(), copy.getAgencyId()) &&
                condition.apply(criteria.getAvatarId(), copy.getAvatarId()) &&
                condition.apply(criteria.getInternalUserId(), copy.getInternalUserId()) &&
                condition.apply(criteria.getAppUserId(), copy.getAppUserId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getAgentRolesId(), copy.getAgentRolesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
