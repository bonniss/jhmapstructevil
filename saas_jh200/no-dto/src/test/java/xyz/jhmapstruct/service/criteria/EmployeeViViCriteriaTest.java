package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmployeeViViCriteriaTest {

    @Test
    void newEmployeeViViCriteriaHasAllFiltersNullTest() {
        var employeeViViCriteria = new EmployeeViViCriteria();
        assertThat(employeeViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void employeeViViCriteriaFluentMethodsCreatesFiltersTest() {
        var employeeViViCriteria = new EmployeeViViCriteria();

        setAllFilters(employeeViViCriteria);

        assertThat(employeeViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void employeeViViCriteriaCopyCreatesNullFilterTest() {
        var employeeViViCriteria = new EmployeeViViCriteria();
        var copy = employeeViViCriteria.copy();

        assertThat(employeeViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(employeeViViCriteria)
        );
    }

    @Test
    void employeeViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var employeeViViCriteria = new EmployeeViViCriteria();
        setAllFilters(employeeViViCriteria);

        var copy = employeeViViCriteria.copy();

        assertThat(employeeViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(employeeViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var employeeViViCriteria = new EmployeeViViCriteria();

        assertThat(employeeViViCriteria).hasToString("EmployeeViViCriteria{}");
    }

    private static void setAllFilters(EmployeeViViCriteria employeeViViCriteria) {
        employeeViViCriteria.id();
        employeeViViCriteria.firstName();
        employeeViViCriteria.lastName();
        employeeViViCriteria.email();
        employeeViViCriteria.hireDate();
        employeeViViCriteria.position();
        employeeViViCriteria.tenantId();
        employeeViViCriteria.distinct();
    }

    private static Condition<EmployeeViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EmployeeViViCriteria> copyFiltersAre(
        EmployeeViViCriteria copy,
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
