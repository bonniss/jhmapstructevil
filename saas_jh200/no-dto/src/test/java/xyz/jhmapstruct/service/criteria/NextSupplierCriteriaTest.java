package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextSupplierCriteriaTest {

    @Test
    void newNextSupplierCriteriaHasAllFiltersNullTest() {
        var nextSupplierCriteria = new NextSupplierCriteria();
        assertThat(nextSupplierCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextSupplierCriteriaFluentMethodsCreatesFiltersTest() {
        var nextSupplierCriteria = new NextSupplierCriteria();

        setAllFilters(nextSupplierCriteria);

        assertThat(nextSupplierCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextSupplierCriteriaCopyCreatesNullFilterTest() {
        var nextSupplierCriteria = new NextSupplierCriteria();
        var copy = nextSupplierCriteria.copy();

        assertThat(nextSupplierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierCriteria)
        );
    }

    @Test
    void nextSupplierCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextSupplierCriteria = new NextSupplierCriteria();
        setAllFilters(nextSupplierCriteria);

        var copy = nextSupplierCriteria.copy();

        assertThat(nextSupplierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextSupplierCriteria = new NextSupplierCriteria();

        assertThat(nextSupplierCriteria).hasToString("NextSupplierCriteria{}");
    }

    private static void setAllFilters(NextSupplierCriteria nextSupplierCriteria) {
        nextSupplierCriteria.id();
        nextSupplierCriteria.name();
        nextSupplierCriteria.contactPerson();
        nextSupplierCriteria.email();
        nextSupplierCriteria.phoneNumber();
        nextSupplierCriteria.tenantId();
        nextSupplierCriteria.productsId();
        nextSupplierCriteria.distinct();
    }

    private static Condition<NextSupplierCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextSupplierCriteria> copyFiltersAre(
        NextSupplierCriteria copy,
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
