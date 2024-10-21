package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OlMasterCriteriaTest {

    @Test
    void newOlMasterCriteriaHasAllFiltersNullTest() {
        var olMasterCriteria = new OlMasterCriteria();
        assertThat(olMasterCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void olMasterCriteriaFluentMethodsCreatesFiltersTest() {
        var olMasterCriteria = new OlMasterCriteria();

        setAllFilters(olMasterCriteria);

        assertThat(olMasterCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void olMasterCriteriaCopyCreatesNullFilterTest() {
        var olMasterCriteria = new OlMasterCriteria();
        var copy = olMasterCriteria.copy();

        assertThat(olMasterCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(olMasterCriteria)
        );
    }

    @Test
    void olMasterCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var olMasterCriteria = new OlMasterCriteria();
        setAllFilters(olMasterCriteria);

        var copy = olMasterCriteria.copy();

        assertThat(olMasterCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(olMasterCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var olMasterCriteria = new OlMasterCriteria();

        assertThat(olMasterCriteria).hasToString("OlMasterCriteria{}");
    }

    private static void setAllFilters(OlMasterCriteria olMasterCriteria) {
        olMasterCriteria.id();
        olMasterCriteria.name();
        olMasterCriteria.slug();
        olMasterCriteria.descriptionHeitiga();
        olMasterCriteria.businessType();
        olMasterCriteria.email();
        olMasterCriteria.hotline();
        olMasterCriteria.taxCode();
        olMasterCriteria.contactsJason();
        olMasterCriteria.extensionJason();
        olMasterCriteria.isEnabled();
        olMasterCriteria.addressId();
        olMasterCriteria.addressViId();
        olMasterCriteria.applicationsId();
        olMasterCriteria.distinct();
    }

    private static Condition<OlMasterCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSlug()) &&
                condition.apply(criteria.getDescriptionHeitiga()) &&
                condition.apply(criteria.getBusinessType()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getHotline()) &&
                condition.apply(criteria.getTaxCode()) &&
                condition.apply(criteria.getContactsJason()) &&
                condition.apply(criteria.getExtensionJason()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getAddressId()) &&
                condition.apply(criteria.getAddressViId()) &&
                condition.apply(criteria.getApplicationsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OlMasterCriteria> copyFiltersAre(OlMasterCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSlug(), copy.getSlug()) &&
                condition.apply(criteria.getDescriptionHeitiga(), copy.getDescriptionHeitiga()) &&
                condition.apply(criteria.getBusinessType(), copy.getBusinessType()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getHotline(), copy.getHotline()) &&
                condition.apply(criteria.getTaxCode(), copy.getTaxCode()) &&
                condition.apply(criteria.getContactsJason(), copy.getContactsJason()) &&
                condition.apply(criteria.getExtensionJason(), copy.getExtensionJason()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                condition.apply(criteria.getAddressViId(), copy.getAddressViId()) &&
                condition.apply(criteria.getApplicationsId(), copy.getApplicationsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
