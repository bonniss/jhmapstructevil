package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextSupplierMiMiCriteriaTest {

    @Test
    void newNextSupplierMiMiCriteriaHasAllFiltersNullTest() {
        var nextSupplierMiMiCriteria = new NextSupplierMiMiCriteria();
        assertThat(nextSupplierMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextSupplierMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextSupplierMiMiCriteria = new NextSupplierMiMiCriteria();

        setAllFilters(nextSupplierMiMiCriteria);

        assertThat(nextSupplierMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextSupplierMiMiCriteriaCopyCreatesNullFilterTest() {
        var nextSupplierMiMiCriteria = new NextSupplierMiMiCriteria();
        var copy = nextSupplierMiMiCriteria.copy();

        assertThat(nextSupplierMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierMiMiCriteria)
        );
    }

    @Test
    void nextSupplierMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextSupplierMiMiCriteria = new NextSupplierMiMiCriteria();
        setAllFilters(nextSupplierMiMiCriteria);

        var copy = nextSupplierMiMiCriteria.copy();

        assertThat(nextSupplierMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextSupplierMiMiCriteria = new NextSupplierMiMiCriteria();

        assertThat(nextSupplierMiMiCriteria).hasToString("NextSupplierMiMiCriteria{}");
    }

    private static void setAllFilters(NextSupplierMiMiCriteria nextSupplierMiMiCriteria) {
        nextSupplierMiMiCriteria.id();
        nextSupplierMiMiCriteria.name();
        nextSupplierMiMiCriteria.contactPerson();
        nextSupplierMiMiCriteria.email();
        nextSupplierMiMiCriteria.phoneNumber();
        nextSupplierMiMiCriteria.tenantId();
        nextSupplierMiMiCriteria.productsId();
        nextSupplierMiMiCriteria.distinct();
    }

    private static Condition<NextSupplierMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextSupplierMiMiCriteria> copyFiltersAre(
        NextSupplierMiMiCriteria copy,
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
