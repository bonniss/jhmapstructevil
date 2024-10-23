package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierGammaCriteriaTest {

    @Test
    void newSupplierGammaCriteriaHasAllFiltersNullTest() {
        var supplierGammaCriteria = new SupplierGammaCriteria();
        assertThat(supplierGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void supplierGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierGammaCriteria = new SupplierGammaCriteria();

        setAllFilters(supplierGammaCriteria);

        assertThat(supplierGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void supplierGammaCriteriaCopyCreatesNullFilterTest() {
        var supplierGammaCriteria = new SupplierGammaCriteria();
        var copy = supplierGammaCriteria.copy();

        assertThat(supplierGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(supplierGammaCriteria)
        );
    }

    @Test
    void supplierGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierGammaCriteria = new SupplierGammaCriteria();
        setAllFilters(supplierGammaCriteria);

        var copy = supplierGammaCriteria.copy();

        assertThat(supplierGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(supplierGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierGammaCriteria = new SupplierGammaCriteria();

        assertThat(supplierGammaCriteria).hasToString("SupplierGammaCriteria{}");
    }

    private static void setAllFilters(SupplierGammaCriteria supplierGammaCriteria) {
        supplierGammaCriteria.id();
        supplierGammaCriteria.name();
        supplierGammaCriteria.contactPerson();
        supplierGammaCriteria.email();
        supplierGammaCriteria.phoneNumber();
        supplierGammaCriteria.tenantId();
        supplierGammaCriteria.productsId();
        supplierGammaCriteria.distinct();
    }

    private static Condition<SupplierGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SupplierGammaCriteria> copyFiltersAre(
        SupplierGammaCriteria copy,
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
