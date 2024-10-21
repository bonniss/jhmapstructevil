package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlAppleViCriteriaTest {

    @Test
    void newAlAppleViCriteriaHasAllFiltersNullTest() {
        var alAppleViCriteria = new AlAppleViCriteria();
        assertThat(alAppleViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alAppleViCriteriaFluentMethodsCreatesFiltersTest() {
        var alAppleViCriteria = new AlAppleViCriteria();

        setAllFilters(alAppleViCriteria);

        assertThat(alAppleViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alAppleViCriteriaCopyCreatesNullFilterTest() {
        var alAppleViCriteria = new AlAppleViCriteria();
        var copy = alAppleViCriteria.copy();

        assertThat(alAppleViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alAppleViCriteria)
        );
    }

    @Test
    void alAppleViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alAppleViCriteria = new AlAppleViCriteria();
        setAllFilters(alAppleViCriteria);

        var copy = alAppleViCriteria.copy();

        assertThat(alAppleViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alAppleViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alAppleViCriteria = new AlAppleViCriteria();

        assertThat(alAppleViCriteria).hasToString("AlAppleViCriteria{}");
    }

    private static void setAllFilters(AlAppleViCriteria alAppleViCriteria) {
        alAppleViCriteria.id();
        alAppleViCriteria.name();
        alAppleViCriteria.description();
        alAppleViCriteria.hotline();
        alAppleViCriteria.taxCode();
        alAppleViCriteria.contactsJason();
        alAppleViCriteria.extensionJason();
        alAppleViCriteria.isEnabled();
        alAppleViCriteria.addressId();
        alAppleViCriteria.agencyTypeId();
        alAppleViCriteria.logoId();
        alAppleViCriteria.applicationId();
        alAppleViCriteria.agentsId();
        alAppleViCriteria.distinct();
    }

    private static Condition<AlAppleViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getHotline()) &&
                condition.apply(criteria.getTaxCode()) &&
                condition.apply(criteria.getContactsJason()) &&
                condition.apply(criteria.getExtensionJason()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getAddressId()) &&
                condition.apply(criteria.getAgencyTypeId()) &&
                condition.apply(criteria.getLogoId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getAgentsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlAppleViCriteria> copyFiltersAre(AlAppleViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getHotline(), copy.getHotline()) &&
                condition.apply(criteria.getTaxCode(), copy.getTaxCode()) &&
                condition.apply(criteria.getContactsJason(), copy.getContactsJason()) &&
                condition.apply(criteria.getExtensionJason(), copy.getExtensionJason()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                condition.apply(criteria.getAgencyTypeId(), copy.getAgencyTypeId()) &&
                condition.apply(criteria.getLogoId(), copy.getLogoId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getAgentsId(), copy.getAgentsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
