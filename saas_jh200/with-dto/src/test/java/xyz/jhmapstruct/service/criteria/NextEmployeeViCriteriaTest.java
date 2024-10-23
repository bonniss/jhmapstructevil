package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextEmployeeViCriteriaTest {

    @Test
    void newNextEmployeeViCriteriaHasAllFiltersNullTest() {
        var nextEmployeeViCriteria = new NextEmployeeViCriteria();
        assertThat(nextEmployeeViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextEmployeeViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextEmployeeViCriteria = new NextEmployeeViCriteria();

        setAllFilters(nextEmployeeViCriteria);

        assertThat(nextEmployeeViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextEmployeeViCriteriaCopyCreatesNullFilterTest() {
        var nextEmployeeViCriteria = new NextEmployeeViCriteria();
        var copy = nextEmployeeViCriteria.copy();

        assertThat(nextEmployeeViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeViCriteria)
        );
    }

    @Test
    void nextEmployeeViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextEmployeeViCriteria = new NextEmployeeViCriteria();
        setAllFilters(nextEmployeeViCriteria);

        var copy = nextEmployeeViCriteria.copy();

        assertThat(nextEmployeeViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextEmployeeViCriteria = new NextEmployeeViCriteria();

        assertThat(nextEmployeeViCriteria).hasToString("NextEmployeeViCriteria{}");
    }

    private static void setAllFilters(NextEmployeeViCriteria nextEmployeeViCriteria) {
        nextEmployeeViCriteria.id();
        nextEmployeeViCriteria.firstName();
        nextEmployeeViCriteria.lastName();
        nextEmployeeViCriteria.email();
        nextEmployeeViCriteria.hireDate();
        nextEmployeeViCriteria.position();
        nextEmployeeViCriteria.tenantId();
        nextEmployeeViCriteria.distinct();
    }

    private static Condition<NextEmployeeViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextEmployeeViCriteria> copyFiltersAre(
        NextEmployeeViCriteria copy,
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
