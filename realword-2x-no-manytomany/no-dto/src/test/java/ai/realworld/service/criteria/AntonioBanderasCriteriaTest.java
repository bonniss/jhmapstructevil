package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AntonioBanderasCriteriaTest {

    @Test
    void newAntonioBanderasCriteriaHasAllFiltersNullTest() {
        var antonioBanderasCriteria = new AntonioBanderasCriteria();
        assertThat(antonioBanderasCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void antonioBanderasCriteriaFluentMethodsCreatesFiltersTest() {
        var antonioBanderasCriteria = new AntonioBanderasCriteria();

        setAllFilters(antonioBanderasCriteria);

        assertThat(antonioBanderasCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void antonioBanderasCriteriaCopyCreatesNullFilterTest() {
        var antonioBanderasCriteria = new AntonioBanderasCriteria();
        var copy = antonioBanderasCriteria.copy();

        assertThat(antonioBanderasCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(antonioBanderasCriteria)
        );
    }

    @Test
    void antonioBanderasCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var antonioBanderasCriteria = new AntonioBanderasCriteria();
        setAllFilters(antonioBanderasCriteria);

        var copy = antonioBanderasCriteria.copy();

        assertThat(antonioBanderasCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(antonioBanderasCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var antonioBanderasCriteria = new AntonioBanderasCriteria();

        assertThat(antonioBanderasCriteria).hasToString("AntonioBanderasCriteria{}");
    }

    private static void setAllFilters(AntonioBanderasCriteria antonioBanderasCriteria) {
        antonioBanderasCriteria.id();
        antonioBanderasCriteria.level();
        antonioBanderasCriteria.code();
        antonioBanderasCriteria.name();
        antonioBanderasCriteria.fullName();
        antonioBanderasCriteria.nativeName();
        antonioBanderasCriteria.officialCode();
        antonioBanderasCriteria.divisionTerm();
        antonioBanderasCriteria.isDeleted();
        antonioBanderasCriteria.currentId();
        antonioBanderasCriteria.parentId();
        antonioBanderasCriteria.childrenId();
        antonioBanderasCriteria.antonioBanderasId();
        antonioBanderasCriteria.distinct();
    }

    private static Condition<AntonioBanderasCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLevel()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getNativeName()) &&
                condition.apply(criteria.getOfficialCode()) &&
                condition.apply(criteria.getDivisionTerm()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCurrentId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getAntonioBanderasId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AntonioBanderasCriteria> copyFiltersAre(
        AntonioBanderasCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLevel(), copy.getLevel()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getNativeName(), copy.getNativeName()) &&
                condition.apply(criteria.getOfficialCode(), copy.getOfficialCode()) &&
                condition.apply(criteria.getDivisionTerm(), copy.getDivisionTerm()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCurrentId(), copy.getCurrentId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getAntonioBanderasId(), copy.getAntonioBanderasId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
