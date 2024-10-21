package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlCatalinaCriteriaTest {

    @Test
    void newAlCatalinaCriteriaHasAllFiltersNullTest() {
        var alCatalinaCriteria = new AlCatalinaCriteria();
        assertThat(alCatalinaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alCatalinaCriteriaFluentMethodsCreatesFiltersTest() {
        var alCatalinaCriteria = new AlCatalinaCriteria();

        setAllFilters(alCatalinaCriteria);

        assertThat(alCatalinaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alCatalinaCriteriaCopyCreatesNullFilterTest() {
        var alCatalinaCriteria = new AlCatalinaCriteria();
        var copy = alCatalinaCriteria.copy();

        assertThat(alCatalinaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alCatalinaCriteria)
        );
    }

    @Test
    void alCatalinaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alCatalinaCriteria = new AlCatalinaCriteria();
        setAllFilters(alCatalinaCriteria);

        var copy = alCatalinaCriteria.copy();

        assertThat(alCatalinaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alCatalinaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alCatalinaCriteria = new AlCatalinaCriteria();

        assertThat(alCatalinaCriteria).hasToString("AlCatalinaCriteria{}");
    }

    private static void setAllFilters(AlCatalinaCriteria alCatalinaCriteria) {
        alCatalinaCriteria.id();
        alCatalinaCriteria.name();
        alCatalinaCriteria.description();
        alCatalinaCriteria.treeDepth();
        alCatalinaCriteria.parentId();
        alCatalinaCriteria.avatarId();
        alCatalinaCriteria.applicationId();
        alCatalinaCriteria.childrenId();
        alCatalinaCriteria.distinct();
    }

    private static Condition<AlCatalinaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getTreeDepth()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getAvatarId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlCatalinaCriteria> copyFiltersAre(AlCatalinaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getTreeDepth(), copy.getTreeDepth()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getAvatarId(), copy.getAvatarId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
