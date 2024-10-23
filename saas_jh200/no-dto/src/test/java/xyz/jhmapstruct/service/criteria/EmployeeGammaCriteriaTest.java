package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmployeeGammaCriteriaTest {

    @Test
    void newEmployeeGammaCriteriaHasAllFiltersNullTest() {
        var employeeGammaCriteria = new EmployeeGammaCriteria();
        assertThat(employeeGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void employeeGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var employeeGammaCriteria = new EmployeeGammaCriteria();

        setAllFilters(employeeGammaCriteria);

        assertThat(employeeGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void employeeGammaCriteriaCopyCreatesNullFilterTest() {
        var employeeGammaCriteria = new EmployeeGammaCriteria();
        var copy = employeeGammaCriteria.copy();

        assertThat(employeeGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(employeeGammaCriteria)
        );
    }

    @Test
    void employeeGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var employeeGammaCriteria = new EmployeeGammaCriteria();
        setAllFilters(employeeGammaCriteria);

        var copy = employeeGammaCriteria.copy();

        assertThat(employeeGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(employeeGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var employeeGammaCriteria = new EmployeeGammaCriteria();

        assertThat(employeeGammaCriteria).hasToString("EmployeeGammaCriteria{}");
    }

    private static void setAllFilters(EmployeeGammaCriteria employeeGammaCriteria) {
        employeeGammaCriteria.id();
        employeeGammaCriteria.firstName();
        employeeGammaCriteria.lastName();
        employeeGammaCriteria.email();
        employeeGammaCriteria.hireDate();
        employeeGammaCriteria.position();
        employeeGammaCriteria.tenantId();
        employeeGammaCriteria.distinct();
    }

    private static Condition<EmployeeGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EmployeeGammaCriteria> copyFiltersAre(
        EmployeeGammaCriteria copy,
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
