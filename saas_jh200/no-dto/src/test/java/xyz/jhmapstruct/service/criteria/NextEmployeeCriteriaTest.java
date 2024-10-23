package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextEmployeeCriteriaTest {

    @Test
    void newNextEmployeeCriteriaHasAllFiltersNullTest() {
        var nextEmployeeCriteria = new NextEmployeeCriteria();
        assertThat(nextEmployeeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextEmployeeCriteriaFluentMethodsCreatesFiltersTest() {
        var nextEmployeeCriteria = new NextEmployeeCriteria();

        setAllFilters(nextEmployeeCriteria);

        assertThat(nextEmployeeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextEmployeeCriteriaCopyCreatesNullFilterTest() {
        var nextEmployeeCriteria = new NextEmployeeCriteria();
        var copy = nextEmployeeCriteria.copy();

        assertThat(nextEmployeeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeCriteria)
        );
    }

    @Test
    void nextEmployeeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextEmployeeCriteria = new NextEmployeeCriteria();
        setAllFilters(nextEmployeeCriteria);

        var copy = nextEmployeeCriteria.copy();

        assertThat(nextEmployeeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextEmployeeCriteria = new NextEmployeeCriteria();

        assertThat(nextEmployeeCriteria).hasToString("NextEmployeeCriteria{}");
    }

    private static void setAllFilters(NextEmployeeCriteria nextEmployeeCriteria) {
        nextEmployeeCriteria.id();
        nextEmployeeCriteria.firstName();
        nextEmployeeCriteria.lastName();
        nextEmployeeCriteria.email();
        nextEmployeeCriteria.hireDate();
        nextEmployeeCriteria.position();
        nextEmployeeCriteria.tenantId();
        nextEmployeeCriteria.distinct();
    }

    private static Condition<NextEmployeeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextEmployeeCriteria> copyFiltersAre(
        NextEmployeeCriteria copy,
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
