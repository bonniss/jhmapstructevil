package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextEmployeeBetaCriteriaTest {

    @Test
    void newNextEmployeeBetaCriteriaHasAllFiltersNullTest() {
        var nextEmployeeBetaCriteria = new NextEmployeeBetaCriteria();
        assertThat(nextEmployeeBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextEmployeeBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextEmployeeBetaCriteria = new NextEmployeeBetaCriteria();

        setAllFilters(nextEmployeeBetaCriteria);

        assertThat(nextEmployeeBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextEmployeeBetaCriteriaCopyCreatesNullFilterTest() {
        var nextEmployeeBetaCriteria = new NextEmployeeBetaCriteria();
        var copy = nextEmployeeBetaCriteria.copy();

        assertThat(nextEmployeeBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeBetaCriteria)
        );
    }

    @Test
    void nextEmployeeBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextEmployeeBetaCriteria = new NextEmployeeBetaCriteria();
        setAllFilters(nextEmployeeBetaCriteria);

        var copy = nextEmployeeBetaCriteria.copy();

        assertThat(nextEmployeeBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextEmployeeBetaCriteria = new NextEmployeeBetaCriteria();

        assertThat(nextEmployeeBetaCriteria).hasToString("NextEmployeeBetaCriteria{}");
    }

    private static void setAllFilters(NextEmployeeBetaCriteria nextEmployeeBetaCriteria) {
        nextEmployeeBetaCriteria.id();
        nextEmployeeBetaCriteria.firstName();
        nextEmployeeBetaCriteria.lastName();
        nextEmployeeBetaCriteria.email();
        nextEmployeeBetaCriteria.hireDate();
        nextEmployeeBetaCriteria.position();
        nextEmployeeBetaCriteria.tenantId();
        nextEmployeeBetaCriteria.distinct();
    }

    private static Condition<NextEmployeeBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextEmployeeBetaCriteria> copyFiltersAre(
        NextEmployeeBetaCriteria copy,
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
