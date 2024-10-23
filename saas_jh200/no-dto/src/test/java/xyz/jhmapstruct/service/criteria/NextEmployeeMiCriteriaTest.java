package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextEmployeeMiCriteriaTest {

    @Test
    void newNextEmployeeMiCriteriaHasAllFiltersNullTest() {
        var nextEmployeeMiCriteria = new NextEmployeeMiCriteria();
        assertThat(nextEmployeeMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextEmployeeMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextEmployeeMiCriteria = new NextEmployeeMiCriteria();

        setAllFilters(nextEmployeeMiCriteria);

        assertThat(nextEmployeeMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextEmployeeMiCriteriaCopyCreatesNullFilterTest() {
        var nextEmployeeMiCriteria = new NextEmployeeMiCriteria();
        var copy = nextEmployeeMiCriteria.copy();

        assertThat(nextEmployeeMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeMiCriteria)
        );
    }

    @Test
    void nextEmployeeMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextEmployeeMiCriteria = new NextEmployeeMiCriteria();
        setAllFilters(nextEmployeeMiCriteria);

        var copy = nextEmployeeMiCriteria.copy();

        assertThat(nextEmployeeMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextEmployeeMiCriteria = new NextEmployeeMiCriteria();

        assertThat(nextEmployeeMiCriteria).hasToString("NextEmployeeMiCriteria{}");
    }

    private static void setAllFilters(NextEmployeeMiCriteria nextEmployeeMiCriteria) {
        nextEmployeeMiCriteria.id();
        nextEmployeeMiCriteria.firstName();
        nextEmployeeMiCriteria.lastName();
        nextEmployeeMiCriteria.email();
        nextEmployeeMiCriteria.hireDate();
        nextEmployeeMiCriteria.position();
        nextEmployeeMiCriteria.tenantId();
        nextEmployeeMiCriteria.distinct();
    }

    private static Condition<NextEmployeeMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getHireDate()) &&
                condition.apply(criteria.getPosition()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NextEmployeeMiCriteria> copyFiltersAre(
        NextEmployeeMiCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getHireDate(), copy.getHireDate()) &&
                condition.apply(criteria.getPosition(), copy.getPosition()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
