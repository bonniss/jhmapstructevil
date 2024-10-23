package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierBetaCriteriaTest {

    @Test
    void newSupplierBetaCriteriaHasAllFiltersNullTest() {
        var supplierBetaCriteria = new SupplierBetaCriteria();
        assertThat(supplierBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void supplierBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierBetaCriteria = new SupplierBetaCriteria();

        setAllFilters(supplierBetaCriteria);

        assertThat(supplierBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void supplierBetaCriteriaCopyCreatesNullFilterTest() {
        var supplierBetaCriteria = new SupplierBetaCriteria();
        var copy = supplierBetaCriteria.copy();

        assertThat(supplierBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(supplierBetaCriteria)
        );
    }

    @Test
    void supplierBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierBetaCriteria = new SupplierBetaCriteria();
        setAllFilters(supplierBetaCriteria);

        var copy = supplierBetaCriteria.copy();

        assertThat(supplierBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(supplierBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierBetaCriteria = new SupplierBetaCriteria();

        assertThat(supplierBetaCriteria).hasToString("SupplierBetaCriteria{}");
    }

    private static void setAllFilters(SupplierBetaCriteria supplierBetaCriteria) {
        supplierBetaCriteria.id();
        supplierBetaCriteria.name();
        supplierBetaCriteria.contactPerson();
        supplierBetaCriteria.email();
        supplierBetaCriteria.phoneNumber();
        supplierBetaCriteria.tenantId();
        supplierBetaCriteria.productsId();
        supplierBetaCriteria.distinct();
    }

    private static Condition<SupplierBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SupplierBetaCriteria> copyFiltersAre(
        SupplierBetaCriteria copy,
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
