package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmployeeMiCriteriaTest {

    @Test
    void newEmployeeMiCriteriaHasAllFiltersNullTest() {
        var employeeMiCriteria = new EmployeeMiCriteria();
        assertThat(employeeMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void employeeMiCriteriaFluentMethodsCreatesFiltersTest() {
        var employeeMiCriteria = new EmployeeMiCriteria();

        setAllFilters(employeeMiCriteria);

        assertThat(employeeMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void employeeMiCriteriaCopyCreatesNullFilterTest() {
        var employeeMiCriteria = new EmployeeMiCriteria();
        var copy = employeeMiCriteria.copy();

        assertThat(employeeMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(employeeMiCriteria)
        );
    }

    @Test
    void employeeMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var employeeMiCriteria = new EmployeeMiCriteria();
        setAllFilters(employeeMiCriteria);

        var copy = employeeMiCriteria.copy();

        assertThat(employeeMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(employeeMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var employeeMiCriteria = new EmployeeMiCriteria();

        assertThat(employeeMiCriteria).hasToString("EmployeeMiCriteria{}");
    }

    private static void setAllFilters(EmployeeMiCriteria employeeMiCriteria) {
        employeeMiCriteria.id();
        employeeMiCriteria.firstName();
        employeeMiCriteria.lastName();
        employeeMiCriteria.email();
        employeeMiCriteria.hireDate();
        employeeMiCriteria.position();
        employeeMiCriteria.tenantId();
        employeeMiCriteria.distinct();
    }

    private static Condition<EmployeeMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EmployeeMiCriteria> copyFiltersAre(EmployeeMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
