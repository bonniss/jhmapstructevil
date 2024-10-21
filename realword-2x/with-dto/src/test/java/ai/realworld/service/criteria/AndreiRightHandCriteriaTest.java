package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AndreiRightHandCriteriaTest {

    @Test
    void newAndreiRightHandCriteriaHasAllFiltersNullTest() {
        var andreiRightHandCriteria = new AndreiRightHandCriteria();
        assertThat(andreiRightHandCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void andreiRightHandCriteriaFluentMethodsCreatesFiltersTest() {
        var andreiRightHandCriteria = new AndreiRightHandCriteria();

        setAllFilters(andreiRightHandCriteria);

        assertThat(andreiRightHandCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void andreiRightHandCriteriaCopyCreatesNullFilterTest() {
        var andreiRightHandCriteria = new AndreiRightHandCriteria();
        var copy = andreiRightHandCriteria.copy();

        assertThat(andreiRightHandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(andreiRightHandCriteria)
        );
    }

    @Test
    void andreiRightHandCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var andreiRightHandCriteria = new AndreiRightHandCriteria();
        setAllFilters(andreiRightHandCriteria);

        var copy = andreiRightHandCriteria.copy();

        assertThat(andreiRightHandCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(andreiRightHandCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var andreiRightHandCriteria = new AndreiRightHandCriteria();

        assertThat(andreiRightHandCriteria).hasToString("AndreiRightHandCriteria{}");
    }

    private static void setAllFilters(AndreiRightHandCriteria andreiRightHandCriteria) {
        andreiRightHandCriteria.id();
        andreiRightHandCriteria.details();
        andreiRightHandCriteria.lat();
        andreiRightHandCriteria.lon();
        andreiRightHandCriteria.countryId();
        andreiRightHandCriteria.provinceId();
        andreiRightHandCriteria.districtId();
        andreiRightHandCriteria.wardId();
        andreiRightHandCriteria.distinct();
    }

    private static Condition<AndreiRightHandCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AndreiRightHandCriteria> copyFiltersAre(
        AndreiRightHandCriteria copy,
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
