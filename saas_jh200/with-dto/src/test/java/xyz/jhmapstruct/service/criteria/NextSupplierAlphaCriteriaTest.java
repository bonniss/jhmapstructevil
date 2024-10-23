package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextSupplierAlphaCriteriaTest {

    @Test
    void newNextSupplierAlphaCriteriaHasAllFiltersNullTest() {
        var nextSupplierAlphaCriteria = new NextSupplierAlphaCriteria();
        assertThat(nextSupplierAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextSupplierAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextSupplierAlphaCriteria = new NextSupplierAlphaCriteria();

        setAllFilters(nextSupplierAlphaCriteria);

        assertThat(nextSupplierAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextSupplierAlphaCriteriaCopyCreatesNullFilterTest() {
        var nextSupplierAlphaCriteria = new NextSupplierAlphaCriteria();
        var copy = nextSupplierAlphaCriteria.copy();

        assertThat(nextSupplierAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierAlphaCriteria)
        );
    }

    @Test
    void nextSupplierAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextSupplierAlphaCriteria = new NextSupplierAlphaCriteria();
        setAllFilters(nextSupplierAlphaCriteria);

        var copy = nextSupplierAlphaCriteria.copy();

        assertThat(nextSupplierAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextSupplierAlphaCriteria = new NextSupplierAlphaCriteria();

        assertThat(nextSupplierAlphaCriteria).hasToString("NextSupplierAlphaCriteria{}");
    }

    private static void setAllFilters(NextSupplierAlphaCriteria nextSupplierAlphaCriteria) {
        nextSupplierAlphaCriteria.id();
        nextSupplierAlphaCriteria.name();
        nextSupplierAlphaCriteria.contactPerson();
        nextSupplierAlphaCriteria.email();
        nextSupplierAlphaCriteria.phoneNumber();
        nextSupplierAlphaCriteria.tenantId();
        nextSupplierAlphaCriteria.productsId();
        nextSupplierAlphaCriteria.distinct();
    }

    private static Condition<NextSupplierAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextSupplierAlphaCriteria> copyFiltersAre(
        NextSupplierAlphaCriteria copy,
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
