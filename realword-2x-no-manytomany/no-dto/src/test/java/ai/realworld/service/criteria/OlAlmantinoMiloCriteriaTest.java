package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OlAlmantinoMiloCriteriaTest {

    @Test
    void newOlAlmantinoMiloCriteriaHasAllFiltersNullTest() {
        var olAlmantinoMiloCriteria = new OlAlmantinoMiloCriteria();
        assertThat(olAlmantinoMiloCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void olAlmantinoMiloCriteriaFluentMethodsCreatesFiltersTest() {
        var olAlmantinoMiloCriteria = new OlAlmantinoMiloCriteria();

        setAllFilters(olAlmantinoMiloCriteria);

        assertThat(olAlmantinoMiloCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void olAlmantinoMiloCriteriaCopyCreatesNullFilterTest() {
        var olAlmantinoMiloCriteria = new OlAlmantinoMiloCriteria();
        var copy = olAlmantinoMiloCriteria.copy();

        assertThat(olAlmantinoMiloCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(olAlmantinoMiloCriteria)
        );
    }

    @Test
    void olAlmantinoMiloCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var olAlmantinoMiloCriteria = new OlAlmantinoMiloCriteria();
        setAllFilters(olAlmantinoMiloCriteria);

        var copy = olAlmantinoMiloCriteria.copy();

        assertThat(olAlmantinoMiloCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(olAlmantinoMiloCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var olAlmantinoMiloCriteria = new OlAlmantinoMiloCriteria();

        assertThat(olAlmantinoMiloCriteria).hasToString("OlAlmantinoMiloCriteria{}");
    }

    private static void setAllFilters(OlAlmantinoMiloCriteria olAlmantinoMiloCriteria) {
        olAlmantinoMiloCriteria.id();
        olAlmantinoMiloCriteria.provider();
        olAlmantinoMiloCriteria.providerAppManagerId();
        olAlmantinoMiloCriteria.name();
        olAlmantinoMiloCriteria.providerSecretKey();
        olAlmantinoMiloCriteria.providerToken();
        olAlmantinoMiloCriteria.providerRefreshToken();
        olAlmantinoMiloCriteria.organizationId();
        olAlmantinoMiloCriteria.applicationsId();
        olAlmantinoMiloCriteria.distinct();
    }

    private static Condition<OlAlmantinoMiloCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProvider()) &&
                condition.apply(criteria.getProviderAppManagerId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getProviderSecretKey()) &&
                condition.apply(criteria.getProviderToken()) &&
                condition.apply(criteria.getProviderRefreshToken()) &&
                condition.apply(criteria.getOrganizationId()) &&
                condition.apply(criteria.getApplicationsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OlAlmantinoMiloCriteria> copyFiltersAre(
        OlAlmantinoMiloCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProvider(), copy.getProvider()) &&
                condition.apply(criteria.getProviderAppManagerId(), copy.getProviderAppManagerId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getProviderSecretKey(), copy.getProviderSecretKey()) &&
                condition.apply(criteria.getProviderToken(), copy.getProviderToken()) &&
                condition.apply(criteria.getProviderRefreshToken(), copy.getProviderRefreshToken()) &&
                condition.apply(criteria.getOrganizationId(), copy.getOrganizationId()) &&
                condition.apply(criteria.getApplicationsId(), copy.getApplicationsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
