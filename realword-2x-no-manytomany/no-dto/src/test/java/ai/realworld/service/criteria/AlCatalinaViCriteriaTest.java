package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlCatalinaViCriteriaTest {

    @Test
    void newAlCatalinaViCriteriaHasAllFiltersNullTest() {
        var alCatalinaViCriteria = new AlCatalinaViCriteria();
        assertThat(alCatalinaViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alCatalinaViCriteriaFluentMethodsCreatesFiltersTest() {
        var alCatalinaViCriteria = new AlCatalinaViCriteria();

        setAllFilters(alCatalinaViCriteria);

        assertThat(alCatalinaViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alCatalinaViCriteriaCopyCreatesNullFilterTest() {
        var alCatalinaViCriteria = new AlCatalinaViCriteria();
        var copy = alCatalinaViCriteria.copy();

        assertThat(alCatalinaViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alCatalinaViCriteria)
        );
    }

    @Test
    void alCatalinaViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alCatalinaViCriteria = new AlCatalinaViCriteria();
        setAllFilters(alCatalinaViCriteria);

        var copy = alCatalinaViCriteria.copy();

        assertThat(alCatalinaViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alCatalinaViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alCatalinaViCriteria = new AlCatalinaViCriteria();

        assertThat(alCatalinaViCriteria).hasToString("AlCatalinaViCriteria{}");
    }

    private static void setAllFilters(AlCatalinaViCriteria alCatalinaViCriteria) {
        alCatalinaViCriteria.id();
        alCatalinaViCriteria.name();
        alCatalinaViCriteria.description();
        alCatalinaViCriteria.treeDepth();
        alCatalinaViCriteria.parentId();
        alCatalinaViCriteria.avatarId();
        alCatalinaViCriteria.childrenId();
        alCatalinaViCriteria.distinct();
    }

    private static Condition<AlCatalinaViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getTreeDepth()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getAvatarId()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlCatalinaViCriteria> copyFiltersAre(
        AlCatalinaViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getTreeDepth(), copy.getTreeDepth()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getAvatarId(), copy.getAvatarId()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
