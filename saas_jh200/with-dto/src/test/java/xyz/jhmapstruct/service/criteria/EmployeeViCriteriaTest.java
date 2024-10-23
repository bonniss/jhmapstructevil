package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmployeeViCriteriaTest {

    @Test
    void newEmployeeViCriteriaHasAllFiltersNullTest() {
        var employeeViCriteria = new EmployeeViCriteria();
        assertThat(employeeViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void employeeViCriteriaFluentMethodsCreatesFiltersTest() {
        var employeeViCriteria = new EmployeeViCriteria();

        setAllFilters(employeeViCriteria);

        assertThat(employeeViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void employeeViCriteriaCopyCreatesNullFilterTest() {
        var employeeViCriteria = new EmployeeViCriteria();
        var copy = employeeViCriteria.copy();

        assertThat(employeeViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(employeeViCriteria)
        );
    }

    @Test
    void employeeViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var employeeViCriteria = new EmployeeViCriteria();
        setAllFilters(employeeViCriteria);

        var copy = employeeViCriteria.copy();

        assertThat(employeeViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(employeeViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var employeeViCriteria = new EmployeeViCriteria();

        assertThat(employeeViCriteria).hasToString("EmployeeViCriteria{}");
    }

    private static void setAllFilters(EmployeeViCriteria employeeViCriteria) {
        employeeViCriteria.id();
        employeeViCriteria.firstName();
        employeeViCriteria.lastName();
        employeeViCriteria.email();
        employeeViCriteria.hireDate();
        employeeViCriteria.position();
        employeeViCriteria.tenantId();
        employeeViCriteria.distinct();
    }

    private static Condition<EmployeeViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EmployeeViCriteria> copyFiltersAre(EmployeeViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
