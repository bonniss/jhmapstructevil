package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierMiCriteriaTest {

    @Test
    void newSupplierMiCriteriaHasAllFiltersNullTest() {
        var supplierMiCriteria = new SupplierMiCriteria();
        assertThat(supplierMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void supplierMiCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierMiCriteria = new SupplierMiCriteria();

        setAllFilters(supplierMiCriteria);

        assertThat(supplierMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void supplierMiCriteriaCopyCreatesNullFilterTest() {
        var supplierMiCriteria = new SupplierMiCriteria();
        var copy = supplierMiCriteria.copy();

        assertThat(supplierMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(supplierMiCriteria)
        );
    }

    @Test
    void supplierMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierMiCriteria = new SupplierMiCriteria();
        setAllFilters(supplierMiCriteria);

        var copy = supplierMiCriteria.copy();

        assertThat(supplierMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(supplierMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierMiCriteria = new SupplierMiCriteria();

        assertThat(supplierMiCriteria).hasToString("SupplierMiCriteria{}");
    }

    private static void setAllFilters(SupplierMiCriteria supplierMiCriteria) {
        supplierMiCriteria.id();
        supplierMiCriteria.name();
        supplierMiCriteria.contactPerson();
        supplierMiCriteria.email();
        supplierMiCriteria.phoneNumber();
        supplierMiCriteria.tenantId();
        supplierMiCriteria.productsId();
        supplierMiCriteria.distinct();
    }

    private static Condition<SupplierMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getContactPerson()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getProductsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SupplierMiCriteria> copyFiltersAre(SupplierMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getContactPerson(), copy.getContactPerson()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getProductsId(), copy.getProductsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
