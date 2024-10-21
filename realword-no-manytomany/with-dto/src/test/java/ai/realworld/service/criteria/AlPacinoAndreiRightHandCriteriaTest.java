package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPacinoAndreiRightHandCriteriaTest {

    @Test
    void newAlPacinoAndreiRightHandCriteriaHasAllFiltersNullTest() {
        var alPacinoAndreiRightHandCriteria = new AlPacinoAndreiRightHandCriteria();
        assertThat(alPacinoAndreiRightHandCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPacinoAndreiRightHandCriteriaFluentMethodsCreatesFiltersTest() {
        var alPacinoAndreiRightHandCriteria = new AlPacinoAndreiRightHandCriteria();

        setAllFilters(alPacinoAndreiRightHandCriteria);

        assertThat(alPacinoAndreiRightHandCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPacinoAndreiRightHandCriteriaCopyCreatesNullFilterTest() {
        var alPacinoAndreiRightHandCriteria = new AlPacinoAndreiRightHandCriteria();
        var copy = alPacinoAndreiRightHandCriteria.copy();

        assertThat(alPacinoAndreiRightHandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoAndreiRightHandCriteria)
        );
    }

    @Test
    void alPacinoAndreiRightHandCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPacinoAndreiRightHandCriteria = new AlPacinoAndreiRightHandCriteria();
        setAllFilters(alPacinoAndreiRightHandCriteria);

        var copy = alPacinoAndreiRightHandCriteria.copy();

        assertThat(alPacinoAndreiRightHandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoAndreiRightHandCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPacinoAndreiRightHandCriteria = new AlPacinoAndreiRightHandCriteria();

        assertThat(alPacinoAndreiRightHandCriteria).hasToString("AlPacinoAndreiRightHandCriteria{}");
    }

    private static void setAllFilters(AlPacinoAndreiRightHandCriteria alPacinoAndreiRightHandCriteria) {
        alPacinoAndreiRightHandCriteria.id();
        alPacinoAndreiRightHandCriteria.name();
        alPacinoAndreiRightHandCriteria.isDefault();
        alPacinoAndreiRightHandCriteria.userId();
        alPacinoAndreiRightHandCriteria.addressId();
        alPacinoAndreiRightHandCriteria.distinct();
    }

    private static Condition<AlPacinoAndreiRightHandCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlPacinoAndreiRightHandCriteria> copyFiltersAre(
        AlPacinoAndreiRightHandCriteria copy,
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
