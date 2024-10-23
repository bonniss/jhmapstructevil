package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierViViCriteriaTest {

    @Test
    void newSupplierViViCriteriaHasAllFiltersNullTest() {
        var supplierViViCriteria = new SupplierViViCriteria();
        assertThat(supplierViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void supplierViViCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierViViCriteria = new SupplierViViCriteria();

        setAllFilters(supplierViViCriteria);

        assertThat(supplierViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void supplierViViCriteriaCopyCreatesNullFilterTest() {
        var supplierViViCriteria = new SupplierViViCriteria();
        var copy = supplierViViCriteria.copy();

        assertThat(supplierViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(supplierViViCriteria)
        );
    }

    @Test
    void supplierViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierViViCriteria = new SupplierViViCriteria();
        setAllFilters(supplierViViCriteria);

        var copy = supplierViViCriteria.copy();

        assertThat(supplierViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(supplierViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierViViCriteria = new SupplierViViCriteria();

        assertThat(supplierViViCriteria).hasToString("SupplierViViCriteria{}");
    }

    private static void setAllFilters(SupplierViViCriteria supplierViViCriteria) {
        supplierViViCriteria.id();
        supplierViViCriteria.name();
        supplierViViCriteria.contactPerson();
        supplierViViCriteria.email();
        supplierViViCriteria.phoneNumber();
        supplierViViCriteria.tenantId();
        supplierViViCriteria.productsId();
        supplierViViCriteria.distinct();
    }

    private static Condition<SupplierViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SupplierViViCriteria> copyFiltersAre(
        SupplierViViCriteria copy,
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
