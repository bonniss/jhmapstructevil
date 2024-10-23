package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmployeeThetaCriteriaTest {

    @Test
    void newEmployeeThetaCriteriaHasAllFiltersNullTest() {
        var employeeThetaCriteria = new EmployeeThetaCriteria();
        assertThat(employeeThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void employeeThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var employeeThetaCriteria = new EmployeeThetaCriteria();

        setAllFilters(employeeThetaCriteria);

        assertThat(employeeThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void employeeThetaCriteriaCopyCreatesNullFilterTest() {
        var employeeThetaCriteria = new EmployeeThetaCriteria();
        var copy = employeeThetaCriteria.copy();

        assertThat(employeeThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(employeeThetaCriteria)
        );
    }

    @Test
    void employeeThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var employeeThetaCriteria = new EmployeeThetaCriteria();
        setAllFilters(employeeThetaCriteria);

        var copy = employeeThetaCriteria.copy();

        assertThat(employeeThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(employeeThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var employeeThetaCriteria = new EmployeeThetaCriteria();

        assertThat(employeeThetaCriteria).hasToString("EmployeeThetaCriteria{}");
    }

    private static void setAllFilters(EmployeeThetaCriteria employeeThetaCriteria) {
        employeeThetaCriteria.id();
        employeeThetaCriteria.firstName();
        employeeThetaCriteria.lastName();
        employeeThetaCriteria.email();
        employeeThetaCriteria.hireDate();
        employeeThetaCriteria.position();
        employeeThetaCriteria.tenantId();
        employeeThetaCriteria.distinct();
    }

    private static Condition<EmployeeThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EmployeeThetaCriteria> copyFiltersAre(
        EmployeeThetaCriteria copy,
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
