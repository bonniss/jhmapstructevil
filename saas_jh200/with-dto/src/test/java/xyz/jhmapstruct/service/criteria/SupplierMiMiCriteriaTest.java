package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierMiMiCriteriaTest {

    @Test
    void newSupplierMiMiCriteriaHasAllFiltersNullTest() {
        var supplierMiMiCriteria = new SupplierMiMiCriteria();
        assertThat(supplierMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void supplierMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierMiMiCriteria = new SupplierMiMiCriteria();

        setAllFilters(supplierMiMiCriteria);

        assertThat(supplierMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void supplierMiMiCriteriaCopyCreatesNullFilterTest() {
        var supplierMiMiCriteria = new SupplierMiMiCriteria();
        var copy = supplierMiMiCriteria.copy();

        assertThat(supplierMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(supplierMiMiCriteria)
        );
    }

    @Test
    void supplierMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierMiMiCriteria = new SupplierMiMiCriteria();
        setAllFilters(supplierMiMiCriteria);

        var copy = supplierMiMiCriteria.copy();

        assertThat(supplierMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(supplierMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierMiMiCriteria = new SupplierMiMiCriteria();

        assertThat(supplierMiMiCriteria).hasToString("SupplierMiMiCriteria{}");
    }

    private static void setAllFilters(SupplierMiMiCriteria supplierMiMiCriteria) {
        supplierMiMiCriteria.id();
        supplierMiMiCriteria.name();
        supplierMiMiCriteria.contactPerson();
        supplierMiMiCriteria.email();
        supplierMiMiCriteria.phoneNumber();
        supplierMiMiCriteria.tenantId();
        supplierMiMiCriteria.productsId();
        supplierMiMiCriteria.distinct();
    }

    private static Condition<SupplierMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SupplierMiMiCriteria> copyFiltersAre(
        SupplierMiMiCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
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