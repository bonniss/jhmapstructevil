package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextSupplierGammaCriteriaTest {

    @Test
    void newNextSupplierGammaCriteriaHasAllFiltersNullTest() {
        var nextSupplierGammaCriteria = new NextSupplierGammaCriteria();
        assertThat(nextSupplierGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextSupplierGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextSupplierGammaCriteria = new NextSupplierGammaCriteria();

        setAllFilters(nextSupplierGammaCriteria);

        assertThat(nextSupplierGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextSupplierGammaCriteriaCopyCreatesNullFilterTest() {
        var nextSupplierGammaCriteria = new NextSupplierGammaCriteria();
        var copy = nextSupplierGammaCriteria.copy();

        assertThat(nextSupplierGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierGammaCriteria)
        );
    }

    @Test
    void nextSupplierGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextSupplierGammaCriteria = new NextSupplierGammaCriteria();
        setAllFilters(nextSupplierGammaCriteria);

        var copy = nextSupplierGammaCriteria.copy();

        assertThat(nextSupplierGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextSupplierGammaCriteria = new NextSupplierGammaCriteria();

        assertThat(nextSupplierGammaCriteria).hasToString("NextSupplierGammaCriteria{}");
    }

    private static void setAllFilters(NextSupplierGammaCriteria nextSupplierGammaCriteria) {
        nextSupplierGammaCriteria.id();
        nextSupplierGammaCriteria.name();
        nextSupplierGammaCriteria.contactPerson();
        nextSupplierGammaCriteria.email();
        nextSupplierGammaCriteria.phoneNumber();
        nextSupplierGammaCriteria.tenantId();
        nextSupplierGammaCriteria.productsId();
        nextSupplierGammaCriteria.distinct();
    }

    private static Condition<NextSupplierGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextSupplierGammaCriteria> copyFiltersAre(
        NextSupplierGammaCriteria copy,
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
