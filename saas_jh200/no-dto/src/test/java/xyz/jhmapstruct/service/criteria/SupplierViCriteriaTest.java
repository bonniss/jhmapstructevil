package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierViCriteriaTest {

    @Test
    void newSupplierViCriteriaHasAllFiltersNullTest() {
        var supplierViCriteria = new SupplierViCriteria();
        assertThat(supplierViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void supplierViCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierViCriteria = new SupplierViCriteria();

        setAllFilters(supplierViCriteria);

        assertThat(supplierViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void supplierViCriteriaCopyCreatesNullFilterTest() {
        var supplierViCriteria = new SupplierViCriteria();
        var copy = supplierViCriteria.copy();

        assertThat(supplierViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(supplierViCriteria)
        );
    }

    @Test
    void supplierViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierViCriteria = new SupplierViCriteria();
        setAllFilters(supplierViCriteria);

        var copy = supplierViCriteria.copy();

        assertThat(supplierViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(supplierViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierViCriteria = new SupplierViCriteria();

        assertThat(supplierViCriteria).hasToString("SupplierViCriteria{}");
    }

    private static void setAllFilters(SupplierViCriteria supplierViCriteria) {
        supplierViCriteria.id();
        supplierViCriteria.name();
        supplierViCriteria.contactPerson();
        supplierViCriteria.email();
        supplierViCriteria.phoneNumber();
        supplierViCriteria.tenantId();
        supplierViCriteria.productsId();
        supplierViCriteria.distinct();
    }

    private static Condition<SupplierViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SupplierViCriteria> copyFiltersAre(SupplierViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
