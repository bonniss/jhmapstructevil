package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPacinoAndreiRightHandViCriteriaTest {

    @Test
    void newAlPacinoAndreiRightHandViCriteriaHasAllFiltersNullTest() {
        var alPacinoAndreiRightHandViCriteria = new AlPacinoAndreiRightHandViCriteria();
        assertThat(alPacinoAndreiRightHandViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPacinoAndreiRightHandViCriteriaFluentMethodsCreatesFiltersTest() {
        var alPacinoAndreiRightHandViCriteria = new AlPacinoAndreiRightHandViCriteria();

        setAllFilters(alPacinoAndreiRightHandViCriteria);

        assertThat(alPacinoAndreiRightHandViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPacinoAndreiRightHandViCriteriaCopyCreatesNullFilterTest() {
        var alPacinoAndreiRightHandViCriteria = new AlPacinoAndreiRightHandViCriteria();
        var copy = alPacinoAndreiRightHandViCriteria.copy();

        assertThat(alPacinoAndreiRightHandViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoAndreiRightHandViCriteria)
        );
    }

    @Test
    void alPacinoAndreiRightHandViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPacinoAndreiRightHandViCriteria = new AlPacinoAndreiRightHandViCriteria();
        setAllFilters(alPacinoAndreiRightHandViCriteria);

        var copy = alPacinoAndreiRightHandViCriteria.copy();

        assertThat(alPacinoAndreiRightHandViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoAndreiRightHandViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPacinoAndreiRightHandViCriteria = new AlPacinoAndreiRightHandViCriteria();

        assertThat(alPacinoAndreiRightHandViCriteria).hasToString("AlPacinoAndreiRightHandViCriteria{}");
    }

    private static void setAllFilters(AlPacinoAndreiRightHandViCriteria alPacinoAndreiRightHandViCriteria) {
        alPacinoAndreiRightHandViCriteria.id();
        alPacinoAndreiRightHandViCriteria.name();
        alPacinoAndreiRightHandViCriteria.isDefault();
        alPacinoAndreiRightHandViCriteria.userId();
        alPacinoAndreiRightHandViCriteria.addressId();
        alPacinoAndreiRightHandViCriteria.distinct();
    }

    private static Condition<AlPacinoAndreiRightHandViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIsDefault()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getAddressId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlPacinoAndreiRightHandViCriteria> copyFiltersAre(
        AlPacinoAndreiRightHandViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIsDefault(), copy.getIsDefault()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
