package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AntonioBanderasViCriteriaTest {

    @Test
    void newAntonioBanderasViCriteriaHasAllFiltersNullTest() {
        var antonioBanderasViCriteria = new AntonioBanderasViCriteria();
        assertThat(antonioBanderasViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void antonioBanderasViCriteriaFluentMethodsCreatesFiltersTest() {
        var antonioBanderasViCriteria = new AntonioBanderasViCriteria();

        setAllFilters(antonioBanderasViCriteria);

        assertThat(antonioBanderasViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void antonioBanderasViCriteriaCopyCreatesNullFilterTest() {
        var antonioBanderasViCriteria = new AntonioBanderasViCriteria();
        var copy = antonioBanderasViCriteria.copy();

        assertThat(antonioBanderasViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(antonioBanderasViCriteria)
        );
    }

    @Test
    void antonioBanderasViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var antonioBanderasViCriteria = new AntonioBanderasViCriteria();
        setAllFilters(antonioBanderasViCriteria);

        var copy = antonioBanderasViCriteria.copy();

        assertThat(antonioBanderasViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(antonioBanderasViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var antonioBanderasViCriteria = new AntonioBanderasViCriteria();

        assertThat(antonioBanderasViCriteria).hasToString("AntonioBanderasViCriteria{}");
    }

    private static void setAllFilters(AntonioBanderasViCriteria antonioBanderasViCriteria) {
        antonioBanderasViCriteria.id();
        antonioBanderasViCriteria.level();
        antonioBanderasViCriteria.code();
        antonioBanderasViCriteria.name();
        antonioBanderasViCriteria.fullName();
        antonioBanderasViCriteria.nativeName();
        antonioBanderasViCriteria.officialCode();
        antonioBanderasViCriteria.divisionTerm();
        antonioBanderasViCriteria.isDeleted();
        antonioBanderasViCriteria.currentId();
        antonioBanderasViCriteria.parentId();
        antonioBanderasViCriteria.childrenId();
        antonioBanderasViCriteria.antonioBanderasViId();
        antonioBanderasViCriteria.distinct();
    }

    private static Condition<AntonioBanderasViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getAntonioBanderasViId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AntonioBanderasViCriteria> copyFiltersAre(
        AntonioBanderasViCriteria copy,
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
                condition.apply(criteria.getAntonioBanderasViId(), copy.getAntonioBanderasViId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
