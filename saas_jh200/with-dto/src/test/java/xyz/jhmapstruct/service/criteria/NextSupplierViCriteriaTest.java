package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextSupplierViCriteriaTest {

    @Test
    void newNextSupplierViCriteriaHasAllFiltersNullTest() {
        var nextSupplierViCriteria = new NextSupplierViCriteria();
        assertThat(nextSupplierViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextSupplierViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextSupplierViCriteria = new NextSupplierViCriteria();

        setAllFilters(nextSupplierViCriteria);

        assertThat(nextSupplierViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextSupplierViCriteriaCopyCreatesNullFilterTest() {
        var nextSupplierViCriteria = new NextSupplierViCriteria();
        var copy = nextSupplierViCriteria.copy();

        assertThat(nextSupplierViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierViCriteria)
        );
    }

    @Test
    void nextSupplierViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextSupplierViCriteria = new NextSupplierViCriteria();
        setAllFilters(nextSupplierViCriteria);

        var copy = nextSupplierViCriteria.copy();

        assertThat(nextSupplierViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextSupplierViCriteria = new NextSupplierViCriteria();

        assertThat(nextSupplierViCriteria).hasToString("NextSupplierViCriteria{}");
    }

    private static void setAllFilters(NextSupplierViCriteria nextSupplierViCriteria) {
        nextSupplierViCriteria.id();
        nextSupplierViCriteria.name();
        nextSupplierViCriteria.contactPerson();
        nextSupplierViCriteria.email();
        nextSupplierViCriteria.phoneNumber();
        nextSupplierViCriteria.tenantId();
        nextSupplierViCriteria.productsId();
        nextSupplierViCriteria.distinct();
    }

    private static Condition<NextSupplierViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextSupplierViCriteria> copyFiltersAre(
        NextSupplierViCriteria copy,
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
