package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextEmployeeAlphaCriteriaTest {

    @Test
    void newNextEmployeeAlphaCriteriaHasAllFiltersNullTest() {
        var nextEmployeeAlphaCriteria = new NextEmployeeAlphaCriteria();
        assertThat(nextEmployeeAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextEmployeeAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextEmployeeAlphaCriteria = new NextEmployeeAlphaCriteria();

        setAllFilters(nextEmployeeAlphaCriteria);

        assertThat(nextEmployeeAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextEmployeeAlphaCriteriaCopyCreatesNullFilterTest() {
        var nextEmployeeAlphaCriteria = new NextEmployeeAlphaCriteria();
        var copy = nextEmployeeAlphaCriteria.copy();

        assertThat(nextEmployeeAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeAlphaCriteria)
        );
    }

    @Test
    void nextEmployeeAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextEmployeeAlphaCriteria = new NextEmployeeAlphaCriteria();
        setAllFilters(nextEmployeeAlphaCriteria);

        var copy = nextEmployeeAlphaCriteria.copy();

        assertThat(nextEmployeeAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextEmployeeAlphaCriteria = new NextEmployeeAlphaCriteria();

        assertThat(nextEmployeeAlphaCriteria).hasToString("NextEmployeeAlphaCriteria{}");
    }

    private static void setAllFilters(NextEmployeeAlphaCriteria nextEmployeeAlphaCriteria) {
        nextEmployeeAlphaCriteria.id();
        nextEmployeeAlphaCriteria.firstName();
        nextEmployeeAlphaCriteria.lastName();
        nextEmployeeAlphaCriteria.email();
        nextEmployeeAlphaCriteria.hireDate();
        nextEmployeeAlphaCriteria.position();
        nextEmployeeAlphaCriteria.tenantId();
        nextEmployeeAlphaCriteria.distinct();
    }

    private static Condition<NextEmployeeAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextEmployeeAlphaCriteria> copyFiltersAre(
        NextEmployeeAlphaCriteria copy,
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
