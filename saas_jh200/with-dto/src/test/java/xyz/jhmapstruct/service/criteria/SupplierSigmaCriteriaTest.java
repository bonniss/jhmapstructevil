package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierSigmaCriteriaTest {

    @Test
    void newSupplierSigmaCriteriaHasAllFiltersNullTest() {
        var supplierSigmaCriteria = new SupplierSigmaCriteria();
        assertThat(supplierSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void supplierSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierSigmaCriteria = new SupplierSigmaCriteria();

        setAllFilters(supplierSigmaCriteria);

        assertThat(supplierSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void supplierSigmaCriteriaCopyCreatesNullFilterTest() {
        var supplierSigmaCriteria = new SupplierSigmaCriteria();
        var copy = supplierSigmaCriteria.copy();

        assertThat(supplierSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(supplierSigmaCriteria)
        );
    }

    @Test
    void supplierSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierSigmaCriteria = new SupplierSigmaCriteria();
        setAllFilters(supplierSigmaCriteria);

        var copy = supplierSigmaCriteria.copy();

        assertThat(supplierSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(supplierSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierSigmaCriteria = new SupplierSigmaCriteria();

        assertThat(supplierSigmaCriteria).hasToString("SupplierSigmaCriteria{}");
    }

    private static void setAllFilters(SupplierSigmaCriteria supplierSigmaCriteria) {
        supplierSigmaCriteria.id();
        supplierSigmaCriteria.name();
        supplierSigmaCriteria.contactPerson();
        supplierSigmaCriteria.email();
        supplierSigmaCriteria.phoneNumber();
        supplierSigmaCriteria.tenantId();
        supplierSigmaCriteria.productsId();
        supplierSigmaCriteria.distinct();
    }

    private static Condition<SupplierSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SupplierSigmaCriteria> copyFiltersAre(
        SupplierSigmaCriteria copy,
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
