package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class JohnLennonCriteriaTest {

    @Test
    void newJohnLennonCriteriaHasAllFiltersNullTest() {
        var johnLennonCriteria = new JohnLennonCriteria();
        assertThat(johnLennonCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void johnLennonCriteriaFluentMethodsCreatesFiltersTest() {
        var johnLennonCriteria = new JohnLennonCriteria();

        setAllFilters(johnLennonCriteria);

        assertThat(johnLennonCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void johnLennonCriteriaCopyCreatesNullFilterTest() {
        var johnLennonCriteria = new JohnLennonCriteria();
        var copy = johnLennonCriteria.copy();

        assertThat(johnLennonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(johnLennonCriteria)
        );
    }

    @Test
    void johnLennonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var johnLennonCriteria = new JohnLennonCriteria();
        setAllFilters(johnLennonCriteria);

        var copy = johnLennonCriteria.copy();

        assertThat(johnLennonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(johnLennonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var johnLennonCriteria = new JohnLennonCriteria();

        assertThat(johnLennonCriteria).hasToString("JohnLennonCriteria{}");
    }

    private static void setAllFilters(JohnLennonCriteria johnLennonCriteria) {
        johnLennonCriteria.id();
        johnLennonCriteria.provider();
        johnLennonCriteria.providerAppId();
        johnLennonCriteria.name();
        johnLennonCriteria.slug();
        johnLennonCriteria.isEnabled();
        johnLennonCriteria.logoId();
        johnLennonCriteria.appManagerId();
        johnLennonCriteria.organizationId();
        johnLennonCriteria.jelloInitiumId();
        johnLennonCriteria.inhouseInitiumId();
        johnLennonCriteria.distinct();
    }

    private static Condition<JohnLennonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProvider()) &&
                condition.apply(criteria.getProviderAppId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSlug()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getLogoId()) &&
                condition.apply(criteria.getAppManagerId()) &&
                condition.apply(criteria.getOrganizationId()) &&
                condition.apply(criteria.getJelloInitiumId()) &&
                condition.apply(criteria.getInhouseInitiumId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<JohnLennonCriteria> copyFiltersAre(JohnLennonCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProvider(), copy.getProvider()) &&
                condition.apply(criteria.getProviderAppId(), copy.getProviderAppId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSlug(), copy.getSlug()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getLogoId(), copy.getLogoId()) &&
                condition.apply(criteria.getAppManagerId(), copy.getAppManagerId()) &&
                condition.apply(criteria.getOrganizationId(), copy.getOrganizationId()) &&
                condition.apply(criteria.getJelloInitiumId(), copy.getJelloInitiumId()) &&
                condition.apply(criteria.getInhouseInitiumId(), copy.getInhouseInitiumId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
