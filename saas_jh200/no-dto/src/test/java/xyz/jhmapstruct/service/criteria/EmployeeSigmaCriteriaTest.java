package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmployeeSigmaCriteriaTest {

    @Test
    void newEmployeeSigmaCriteriaHasAllFiltersNullTest() {
        var employeeSigmaCriteria = new EmployeeSigmaCriteria();
        assertThat(employeeSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void employeeSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var employeeSigmaCriteria = new EmployeeSigmaCriteria();

        setAllFilters(employeeSigmaCriteria);

        assertThat(employeeSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void employeeSigmaCriteriaCopyCreatesNullFilterTest() {
        var employeeSigmaCriteria = new EmployeeSigmaCriteria();
        var copy = employeeSigmaCriteria.copy();

        assertThat(employeeSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(employeeSigmaCriteria)
        );
    }

    @Test
    void employeeSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var employeeSigmaCriteria = new EmployeeSigmaCriteria();
        setAllFilters(employeeSigmaCriteria);

        var copy = employeeSigmaCriteria.copy();

        assertThat(employeeSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(employeeSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var employeeSigmaCriteria = new EmployeeSigmaCriteria();

        assertThat(employeeSigmaCriteria).hasToString("EmployeeSigmaCriteria{}");
    }

    private static void setAllFilters(EmployeeSigmaCriteria employeeSigmaCriteria) {
        employeeSigmaCriteria.id();
        employeeSigmaCriteria.firstName();
        employeeSigmaCriteria.lastName();
        employeeSigmaCriteria.email();
        employeeSigmaCriteria.hireDate();
        employeeSigmaCriteria.position();
        employeeSigmaCriteria.tenantId();
        employeeSigmaCriteria.distinct();
    }

    private static Condition<EmployeeSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EmployeeSigmaCriteria> copyFiltersAre(
        EmployeeSigmaCriteria copy,
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
