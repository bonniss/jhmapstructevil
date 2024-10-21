package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlAppleCriteriaTest {

    @Test
    void newAlAppleCriteriaHasAllFiltersNullTest() {
        var alAppleCriteria = new AlAppleCriteria();
        assertThat(alAppleCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alAppleCriteriaFluentMethodsCreatesFiltersTest() {
        var alAppleCriteria = new AlAppleCriteria();

        setAllFilters(alAppleCriteria);

        assertThat(alAppleCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alAppleCriteriaCopyCreatesNullFilterTest() {
        var alAppleCriteria = new AlAppleCriteria();
        var copy = alAppleCriteria.copy();

        assertThat(alAppleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alAppleCriteria)
        );
    }

    @Test
    void alAppleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alAppleCriteria = new AlAppleCriteria();
        setAllFilters(alAppleCriteria);

        var copy = alAppleCriteria.copy();

        assertThat(alAppleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alAppleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alAppleCriteria = new AlAppleCriteria();

        assertThat(alAppleCriteria).hasToString("AlAppleCriteria{}");
    }

    private static void setAllFilters(AlAppleCriteria alAppleCriteria) {
        alAppleCriteria.id();
        alAppleCriteria.name();
        alAppleCriteria.description();
        alAppleCriteria.hotline();
        alAppleCriteria.taxCode();
        alAppleCriteria.contactsJason();
        alAppleCriteria.extensionJason();
        alAppleCriteria.isEnabled();
        alAppleCriteria.addressId();
        alAppleCriteria.agencyTypeId();
        alAppleCriteria.logoId();
        alAppleCriteria.applicationId();
        alAppleCriteria.agentsId();
        alAppleCriteria.distinct();
    }

    private static Condition<AlAppleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlAppleCriteria> copyFiltersAre(AlAppleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
