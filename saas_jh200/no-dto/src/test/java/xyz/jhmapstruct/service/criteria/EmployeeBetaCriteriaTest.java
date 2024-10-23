package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmployeeBetaCriteriaTest {

    @Test
    void newEmployeeBetaCriteriaHasAllFiltersNullTest() {
        var employeeBetaCriteria = new EmployeeBetaCriteria();
        assertThat(employeeBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void employeeBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var employeeBetaCriteria = new EmployeeBetaCriteria();

        setAllFilters(employeeBetaCriteria);

        assertThat(employeeBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void employeeBetaCriteriaCopyCreatesNullFilterTest() {
        var employeeBetaCriteria = new EmployeeBetaCriteria();
        var copy = employeeBetaCriteria.copy();

        assertThat(employeeBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(employeeBetaCriteria)
        );
    }

    @Test
    void employeeBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var employeeBetaCriteria = new EmployeeBetaCriteria();
        setAllFilters(employeeBetaCriteria);

        var copy = employeeBetaCriteria.copy();

        assertThat(employeeBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(employeeBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var employeeBetaCriteria = new EmployeeBetaCriteria();

        assertThat(employeeBetaCriteria).hasToString("EmployeeBetaCriteria{}");
    }

    private static void setAllFilters(EmployeeBetaCriteria employeeBetaCriteria) {
        employeeBetaCriteria.id();
        employeeBetaCriteria.firstName();
        employeeBetaCriteria.lastName();
        employeeBetaCriteria.email();
        employeeBetaCriteria.hireDate();
        employeeBetaCriteria.position();
        employeeBetaCriteria.tenantId();
        employeeBetaCriteria.distinct();
    }

    private static Condition<EmployeeBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EmployeeBetaCriteria> copyFiltersAre(
        EmployeeBetaCriteria copy,
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
