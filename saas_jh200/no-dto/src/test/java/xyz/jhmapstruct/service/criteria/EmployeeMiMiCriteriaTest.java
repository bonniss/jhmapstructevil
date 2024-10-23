package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmployeeMiMiCriteriaTest {

    @Test
    void newEmployeeMiMiCriteriaHasAllFiltersNullTest() {
        var employeeMiMiCriteria = new EmployeeMiMiCriteria();
        assertThat(employeeMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void employeeMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var employeeMiMiCriteria = new EmployeeMiMiCriteria();

        setAllFilters(employeeMiMiCriteria);

        assertThat(employeeMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void employeeMiMiCriteriaCopyCreatesNullFilterTest() {
        var employeeMiMiCriteria = new EmployeeMiMiCriteria();
        var copy = employeeMiMiCriteria.copy();

        assertThat(employeeMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(employeeMiMiCriteria)
        );
    }

    @Test
    void employeeMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var employeeMiMiCriteria = new EmployeeMiMiCriteria();
        setAllFilters(employeeMiMiCriteria);

        var copy = employeeMiMiCriteria.copy();

        assertThat(employeeMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(employeeMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var employeeMiMiCriteria = new EmployeeMiMiCriteria();

        assertThat(employeeMiMiCriteria).hasToString("EmployeeMiMiCriteria{}");
    }

    private static void setAllFilters(EmployeeMiMiCriteria employeeMiMiCriteria) {
        employeeMiMiCriteria.id();
        employeeMiMiCriteria.firstName();
        employeeMiMiCriteria.lastName();
        employeeMiMiCriteria.email();
        employeeMiMiCriteria.hireDate();
        employeeMiMiCriteria.position();
        employeeMiMiCriteria.tenantId();
        employeeMiMiCriteria.distinct();
    }

    private static Condition<EmployeeMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EmployeeMiMiCriteria> copyFiltersAre(
        EmployeeMiMiCriteria copy,
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
