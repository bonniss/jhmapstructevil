package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextSupplierMiCriteriaTest {

    @Test
    void newNextSupplierMiCriteriaHasAllFiltersNullTest() {
        var nextSupplierMiCriteria = new NextSupplierMiCriteria();
        assertThat(nextSupplierMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextSupplierMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextSupplierMiCriteria = new NextSupplierMiCriteria();

        setAllFilters(nextSupplierMiCriteria);

        assertThat(nextSupplierMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextSupplierMiCriteriaCopyCreatesNullFilterTest() {
        var nextSupplierMiCriteria = new NextSupplierMiCriteria();
        var copy = nextSupplierMiCriteria.copy();

        assertThat(nextSupplierMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierMiCriteria)
        );
    }

    @Test
    void nextSupplierMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextSupplierMiCriteria = new NextSupplierMiCriteria();
        setAllFilters(nextSupplierMiCriteria);

        var copy = nextSupplierMiCriteria.copy();

        assertThat(nextSupplierMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextSupplierMiCriteria = new NextSupplierMiCriteria();

        assertThat(nextSupplierMiCriteria).hasToString("NextSupplierMiCriteria{}");
    }

    private static void setAllFilters(NextSupplierMiCriteria nextSupplierMiCriteria) {
        nextSupplierMiCriteria.id();
        nextSupplierMiCriteria.name();
        nextSupplierMiCriteria.contactPerson();
        nextSupplierMiCriteria.email();
        nextSupplierMiCriteria.phoneNumber();
        nextSupplierMiCriteria.tenantId();
        nextSupplierMiCriteria.productsId();
        nextSupplierMiCriteria.distinct();
    }

    private static Condition<NextSupplierMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextSupplierMiCriteria> copyFiltersAre(
        NextSupplierMiCriteria copy,
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
