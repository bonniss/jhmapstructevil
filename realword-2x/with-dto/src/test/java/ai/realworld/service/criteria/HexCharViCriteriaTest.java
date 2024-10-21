package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HexCharViCriteriaTest {

    @Test
    void newHexCharViCriteriaHasAllFiltersNullTest() {
        var hexCharViCriteria = new HexCharViCriteria();
        assertThat(hexCharViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hexCharViCriteriaFluentMethodsCreatesFiltersTest() {
        var hexCharViCriteria = new HexCharViCriteria();

        setAllFilters(hexCharViCriteria);

        assertThat(hexCharViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hexCharViCriteriaCopyCreatesNullFilterTest() {
        var hexCharViCriteria = new HexCharViCriteria();
        var copy = hexCharViCriteria.copy();

        assertThat(hexCharViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hexCharViCriteria)
        );
    }

    @Test
    void hexCharViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hexCharViCriteria = new HexCharViCriteria();
        setAllFilters(hexCharViCriteria);

        var copy = hexCharViCriteria.copy();

        assertThat(hexCharViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hexCharViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hexCharViCriteria = new HexCharViCriteria();

        assertThat(hexCharViCriteria).hasToString("HexCharViCriteria{}");
    }

    private static void setAllFilters(HexCharViCriteria hexCharViCriteria) {
        hexCharViCriteria.id();
        hexCharViCriteria.dob();
        hexCharViCriteria.gender();
        hexCharViCriteria.phone();
        hexCharViCriteria.bioHeitiga();
        hexCharViCriteria.isEnabled();
        hexCharViCriteria.internalUserId();
        hexCharViCriteria.roleId();
        hexCharViCriteria.distinct();
    }

    private static Condition<HexCharViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDob()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getBioHeitiga()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getInternalUserId()) &&
                condition.apply(criteria.getRoleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HexCharViCriteria> copyFiltersAre(HexCharViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDob(), copy.getDob()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getBioHeitiga(), copy.getBioHeitiga()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getInternalUserId(), copy.getInternalUserId()) &&
                condition.apply(criteria.getRoleId(), copy.getRoleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
