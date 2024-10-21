package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AndreiRightHandViCriteriaTest {

    @Test
    void newAndreiRightHandViCriteriaHasAllFiltersNullTest() {
        var andreiRightHandViCriteria = new AndreiRightHandViCriteria();
        assertThat(andreiRightHandViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void andreiRightHandViCriteriaFluentMethodsCreatesFiltersTest() {
        var andreiRightHandViCriteria = new AndreiRightHandViCriteria();

        setAllFilters(andreiRightHandViCriteria);

        assertThat(andreiRightHandViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void andreiRightHandViCriteriaCopyCreatesNullFilterTest() {
        var andreiRightHandViCriteria = new AndreiRightHandViCriteria();
        var copy = andreiRightHandViCriteria.copy();

        assertThat(andreiRightHandViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(andreiRightHandViCriteria)
        );
    }

    @Test
    void andreiRightHandViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var andreiRightHandViCriteria = new AndreiRightHandViCriteria();
        setAllFilters(andreiRightHandViCriteria);

        var copy = andreiRightHandViCriteria.copy();

        assertThat(andreiRightHandViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(andreiRightHandViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var andreiRightHandViCriteria = new AndreiRightHandViCriteria();

        assertThat(andreiRightHandViCriteria).hasToString("AndreiRightHandViCriteria{}");
    }

    private static void setAllFilters(AndreiRightHandViCriteria andreiRightHandViCriteria) {
        andreiRightHandViCriteria.id();
        andreiRightHandViCriteria.details();
        andreiRightHandViCriteria.lat();
        andreiRightHandViCriteria.lon();
        andreiRightHandViCriteria.countryId();
        andreiRightHandViCriteria.provinceId();
        andreiRightHandViCriteria.districtId();
        andreiRightHandViCriteria.wardId();
        andreiRightHandViCriteria.distinct();
    }

    private static Condition<AndreiRightHandViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDetails()) &&
                condition.apply(criteria.getLat()) &&
                condition.apply(criteria.getLon()) &&
                condition.apply(criteria.getCountryId()) &&
                condition.apply(criteria.getProvinceId()) &&
                condition.apply(criteria.getDistrictId()) &&
                condition.apply(criteria.getWardId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AndreiRightHandViCriteria> copyFiltersAre(
        AndreiRightHandViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDetails(), copy.getDetails()) &&
                condition.apply(criteria.getLat(), copy.getLat()) &&
                condition.apply(criteria.getLon(), copy.getLon()) &&
                condition.apply(criteria.getCountryId(), copy.getCountryId()) &&
                condition.apply(criteria.getProvinceId(), copy.getProvinceId()) &&
                condition.apply(criteria.getDistrictId(), copy.getDistrictId()) &&
                condition.apply(criteria.getWardId(), copy.getWardId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
