package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextSupplierThetaCriteriaTest {

    @Test
    void newNextSupplierThetaCriteriaHasAllFiltersNullTest() {
        var nextSupplierThetaCriteria = new NextSupplierThetaCriteria();
        assertThat(nextSupplierThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextSupplierThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextSupplierThetaCriteria = new NextSupplierThetaCriteria();

        setAllFilters(nextSupplierThetaCriteria);

        assertThat(nextSupplierThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextSupplierThetaCriteriaCopyCreatesNullFilterTest() {
        var nextSupplierThetaCriteria = new NextSupplierThetaCriteria();
        var copy = nextSupplierThetaCriteria.copy();

        assertThat(nextSupplierThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierThetaCriteria)
        );
    }

    @Test
    void nextSupplierThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextSupplierThetaCriteria = new NextSupplierThetaCriteria();
        setAllFilters(nextSupplierThetaCriteria);

        var copy = nextSupplierThetaCriteria.copy();

        assertThat(nextSupplierThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextSupplierThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextSupplierThetaCriteria = new NextSupplierThetaCriteria();

        assertThat(nextSupplierThetaCriteria).hasToString("NextSupplierThetaCriteria{}");
    }

    private static void setAllFilters(NextSupplierThetaCriteria nextSupplierThetaCriteria) {
        nextSupplierThetaCriteria.id();
        nextSupplierThetaCriteria.name();
        nextSupplierThetaCriteria.contactPerson();
        nextSupplierThetaCriteria.email();
        nextSupplierThetaCriteria.phoneNumber();
        nextSupplierThetaCriteria.tenantId();
        nextSupplierThetaCriteria.productsId();
        nextSupplierThetaCriteria.distinct();
    }

    private static Condition<NextSupplierThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextSupplierThetaCriteria> copyFiltersAre(
        NextSupplierThetaCriteria copy,
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
