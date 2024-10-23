package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierThetaCriteriaTest {

    @Test
    void newSupplierThetaCriteriaHasAllFiltersNullTest() {
        var supplierThetaCriteria = new SupplierThetaCriteria();
        assertThat(supplierThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void supplierThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierThetaCriteria = new SupplierThetaCriteria();

        setAllFilters(supplierThetaCriteria);

        assertThat(supplierThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void supplierThetaCriteriaCopyCreatesNullFilterTest() {
        var supplierThetaCriteria = new SupplierThetaCriteria();
        var copy = supplierThetaCriteria.copy();

        assertThat(supplierThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(supplierThetaCriteria)
        );
    }

    @Test
    void supplierThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierThetaCriteria = new SupplierThetaCriteria();
        setAllFilters(supplierThetaCriteria);

        var copy = supplierThetaCriteria.copy();

        assertThat(supplierThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(supplierThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierThetaCriteria = new SupplierThetaCriteria();

        assertThat(supplierThetaCriteria).hasToString("SupplierThetaCriteria{}");
    }

    private static void setAllFilters(SupplierThetaCriteria supplierThetaCriteria) {
        supplierThetaCriteria.id();
        supplierThetaCriteria.name();
        supplierThetaCriteria.contactPerson();
        supplierThetaCriteria.email();
        supplierThetaCriteria.phoneNumber();
        supplierThetaCriteria.tenantId();
        supplierThetaCriteria.productsId();
        supplierThetaCriteria.distinct();
    }

    private static Condition<SupplierThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SupplierThetaCriteria> copyFiltersAre(
        SupplierThetaCriteria copy,
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
