package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextSupplierViViCriteriaTest {

    @Test
    void newNextSupplierViViCriteriaHasAllFiltersNullTest() {
        var nextSupplierViViCriteria = new NextSupplierViViCriteria();
        assertThat(nextSupplierViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextSupplierViViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextSupplierViViCriteria = new NextSupplierViViCriteria();

        setAllFilters(nextSupplierViViCriteria);

        assertThat(nextSupplierViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextSupplierViViCriteriaCopyCreatesNullFilterTest() {
        var nextSupplierViViCriteria = new NextSupplierViViCriteria();
        var copy = nextSupplierViViCriteria.copy();

        assertThat(nextSupplierViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierViViCriteria)
        );
    }

    @Test
    void nextSupplierViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextSupplierViViCriteria = new NextSupplierViViCriteria();
        setAllFilters(nextSupplierViViCriteria);

        var copy = nextSupplierViViCriteria.copy();

        assertThat(nextSupplierViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextSupplierViViCriteria = new NextSupplierViViCriteria();

        assertThat(nextSupplierViViCriteria).hasToString("NextSupplierViViCriteria{}");
    }

    private static void setAllFilters(NextSupplierViViCriteria nextSupplierViViCriteria) {
        nextSupplierViViCriteria.id();
        nextSupplierViViCriteria.name();
        nextSupplierViViCriteria.contactPerson();
        nextSupplierViViCriteria.email();
        nextSupplierViViCriteria.phoneNumber();
        nextSupplierViViCriteria.tenantId();
        nextSupplierViViCriteria.productsId();
        nextSupplierViViCriteria.distinct();
    }

    private static Condition<NextSupplierViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextSupplierViViCriteria> copyFiltersAre(
        NextSupplierViViCriteria copy,
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
