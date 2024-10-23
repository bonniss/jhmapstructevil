package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextEmployeeSigmaCriteriaTest {

    @Test
    void newNextEmployeeSigmaCriteriaHasAllFiltersNullTest() {
        var nextEmployeeSigmaCriteria = new NextEmployeeSigmaCriteria();
        assertThat(nextEmployeeSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextEmployeeSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextEmployeeSigmaCriteria = new NextEmployeeSigmaCriteria();

        setAllFilters(nextEmployeeSigmaCriteria);

        assertThat(nextEmployeeSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextEmployeeSigmaCriteriaCopyCreatesNullFilterTest() {
        var nextEmployeeSigmaCriteria = new NextEmployeeSigmaCriteria();
        var copy = nextEmployeeSigmaCriteria.copy();

        assertThat(nextEmployeeSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeSigmaCriteria)
        );
    }

    @Test
    void nextEmployeeSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextEmployeeSigmaCriteria = new NextEmployeeSigmaCriteria();
        setAllFilters(nextEmployeeSigmaCriteria);

        var copy = nextEmployeeSigmaCriteria.copy();

        assertThat(nextEmployeeSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextEmployeeSigmaCriteria = new NextEmployeeSigmaCriteria();

        assertThat(nextEmployeeSigmaCriteria).hasToString("NextEmployeeSigmaCriteria{}");
    }

    private static void setAllFilters(NextEmployeeSigmaCriteria nextEmployeeSigmaCriteria) {
        nextEmployeeSigmaCriteria.id();
        nextEmployeeSigmaCriteria.firstName();
        nextEmployeeSigmaCriteria.lastName();
        nextEmployeeSigmaCriteria.email();
        nextEmployeeSigmaCriteria.hireDate();
        nextEmployeeSigmaCriteria.position();
        nextEmployeeSigmaCriteria.tenantId();
        nextEmployeeSigmaCriteria.distinct();
    }

    private static Condition<NextEmployeeSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextEmployeeSigmaCriteria> copyFiltersAre(
        NextEmployeeSigmaCriteria copy,
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
