package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HexCharCriteriaTest {

    @Test
    void newHexCharCriteriaHasAllFiltersNullTest() {
        var hexCharCriteria = new HexCharCriteria();
        assertThat(hexCharCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hexCharCriteriaFluentMethodsCreatesFiltersTest() {
        var hexCharCriteria = new HexCharCriteria();

        setAllFilters(hexCharCriteria);

        assertThat(hexCharCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hexCharCriteriaCopyCreatesNullFilterTest() {
        var hexCharCriteria = new HexCharCriteria();
        var copy = hexCharCriteria.copy();

        assertThat(hexCharCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hexCharCriteria)
        );
    }

    @Test
    void hexCharCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hexCharCriteria = new HexCharCriteria();
        setAllFilters(hexCharCriteria);

        var copy = hexCharCriteria.copy();

        assertThat(hexCharCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hexCharCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hexCharCriteria = new HexCharCriteria();

        assertThat(hexCharCriteria).hasToString("HexCharCriteria{}");
    }

    private static void setAllFilters(HexCharCriteria hexCharCriteria) {
        hexCharCriteria.id();
        hexCharCriteria.dob();
        hexCharCriteria.gender();
        hexCharCriteria.phone();
        hexCharCriteria.bioHeitiga();
        hexCharCriteria.isEnabled();
        hexCharCriteria.internalUserId();
        hexCharCriteria.roleId();
        hexCharCriteria.distinct();
    }

    private static Condition<HexCharCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<HexCharCriteria> copyFiltersAre(HexCharCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
