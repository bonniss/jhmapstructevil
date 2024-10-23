package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmployeeAlphaCriteriaTest {

    @Test
    void newEmployeeAlphaCriteriaHasAllFiltersNullTest() {
        var employeeAlphaCriteria = new EmployeeAlphaCriteria();
        assertThat(employeeAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void employeeAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var employeeAlphaCriteria = new EmployeeAlphaCriteria();

        setAllFilters(employeeAlphaCriteria);

        assertThat(employeeAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void employeeAlphaCriteriaCopyCreatesNullFilterTest() {
        var employeeAlphaCriteria = new EmployeeAlphaCriteria();
        var copy = employeeAlphaCriteria.copy();

        assertThat(employeeAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(employeeAlphaCriteria)
        );
    }

    @Test
    void employeeAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var employeeAlphaCriteria = new EmployeeAlphaCriteria();
        setAllFilters(employeeAlphaCriteria);

        var copy = employeeAlphaCriteria.copy();

        assertThat(employeeAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(employeeAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var employeeAlphaCriteria = new EmployeeAlphaCriteria();

        assertThat(employeeAlphaCriteria).hasToString("EmployeeAlphaCriteria{}");
    }

    private static void setAllFilters(EmployeeAlphaCriteria employeeAlphaCriteria) {
        employeeAlphaCriteria.id();
        employeeAlphaCriteria.firstName();
        employeeAlphaCriteria.lastName();
        employeeAlphaCriteria.email();
        employeeAlphaCriteria.hireDate();
        employeeAlphaCriteria.position();
        employeeAlphaCriteria.tenantId();
        employeeAlphaCriteria.distinct();
    }

    private static Condition<EmployeeAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EmployeeAlphaCriteria> copyFiltersAre(
        EmployeeAlphaCriteria copy,
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
