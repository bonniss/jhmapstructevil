package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextSupplierBetaCriteriaTest {

    @Test
    void newNextSupplierBetaCriteriaHasAllFiltersNullTest() {
        var nextSupplierBetaCriteria = new NextSupplierBetaCriteria();
        assertThat(nextSupplierBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextSupplierBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextSupplierBetaCriteria = new NextSupplierBetaCriteria();

        setAllFilters(nextSupplierBetaCriteria);

        assertThat(nextSupplierBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextSupplierBetaCriteriaCopyCreatesNullFilterTest() {
        var nextSupplierBetaCriteria = new NextSupplierBetaCriteria();
        var copy = nextSupplierBetaCriteria.copy();

        assertThat(nextSupplierBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierBetaCriteria)
        );
    }

    @Test
    void nextSupplierBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextSupplierBetaCriteria = new NextSupplierBetaCriteria();
        setAllFilters(nextSupplierBetaCriteria);

        var copy = nextSupplierBetaCriteria.copy();

        assertThat(nextSupplierBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextSupplierBetaCriteria = new NextSupplierBetaCriteria();

        assertThat(nextSupplierBetaCriteria).hasToString("NextSupplierBetaCriteria{}");
    }

    private static void setAllFilters(NextSupplierBetaCriteria nextSupplierBetaCriteria) {
        nextSupplierBetaCriteria.id();
        nextSupplierBetaCriteria.name();
        nextSupplierBetaCriteria.contactPerson();
        nextSupplierBetaCriteria.email();
        nextSupplierBetaCriteria.phoneNumber();
        nextSupplierBetaCriteria.tenantId();
        nextSupplierBetaCriteria.productsId();
        nextSupplierBetaCriteria.distinct();
    }

    private static Condition<NextSupplierBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextSupplierBetaCriteria> copyFiltersAre(
        NextSupplierBetaCriteria copy,
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
