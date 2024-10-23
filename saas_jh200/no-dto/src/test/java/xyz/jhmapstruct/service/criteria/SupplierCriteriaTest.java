package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierCriteriaTest {

    @Test
    void newSupplierCriteriaHasAllFiltersNullTest() {
        var supplierCriteria = new SupplierCriteria();
        assertThat(supplierCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void supplierCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierCriteria = new SupplierCriteria();

        setAllFilters(supplierCriteria);

        assertThat(supplierCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void supplierCriteriaCopyCreatesNullFilterTest() {
        var supplierCriteria = new SupplierCriteria();
        var copy = supplierCriteria.copy();

        assertThat(supplierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(supplierCriteria)
        );
    }

    @Test
    void supplierCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierCriteria = new SupplierCriteria();
        setAllFilters(supplierCriteria);

        var copy = supplierCriteria.copy();

        assertThat(supplierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(supplierCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierCriteria = new SupplierCriteria();

        assertThat(supplierCriteria).hasToString("SupplierCriteria{}");
    }

    private static void setAllFilters(SupplierCriteria supplierCriteria) {
        supplierCriteria.id();
        supplierCriteria.name();
        supplierCriteria.contactPerson();
        supplierCriteria.email();
        supplierCriteria.phoneNumber();
        supplierCriteria.tenantId();
        supplierCriteria.productsId();
        supplierCriteria.distinct();
    }

    private static Condition<SupplierCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SupplierCriteria> copyFiltersAre(SupplierCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
