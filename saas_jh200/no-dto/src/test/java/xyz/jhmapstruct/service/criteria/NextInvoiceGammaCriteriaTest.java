package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextInvoiceGammaCriteriaTest {

    @Test
    void newNextInvoiceGammaCriteriaHasAllFiltersNullTest() {
        var nextInvoiceGammaCriteria = new NextInvoiceGammaCriteria();
        assertThat(nextInvoiceGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextInvoiceGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextInvoiceGammaCriteria = new NextInvoiceGammaCriteria();

        setAllFilters(nextInvoiceGammaCriteria);

        assertThat(nextInvoiceGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextInvoiceGammaCriteriaCopyCreatesNullFilterTest() {
        var nextInvoiceGammaCriteria = new NextInvoiceGammaCriteria();
        var copy = nextInvoiceGammaCriteria.copy();

        assertThat(nextInvoiceGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceGammaCriteria)
        );
    }

    @Test
    void nextInvoiceGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextInvoiceGammaCriteria = new NextInvoiceGammaCriteria();
        setAllFilters(nextInvoiceGammaCriteria);

        var copy = nextInvoiceGammaCriteria.copy();

        assertThat(nextInvoiceGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextInvoiceGammaCriteria = new NextInvoiceGammaCriteria();

        assertThat(nextInvoiceGammaCriteria).hasToString("NextInvoiceGammaCriteria{}");
    }

    private static void setAllFilters(NextInvoiceGammaCriteria nextInvoiceGammaCriteria) {
        nextInvoiceGammaCriteria.id();
        nextInvoiceGammaCriteria.invoiceNumber();
        nextInvoiceGammaCriteria.issueDate();
        nextInvoiceGammaCriteria.dueDate();
        nextInvoiceGammaCriteria.amount();
        nextInvoiceGammaCriteria.tenantId();
        nextInvoiceGammaCriteria.distinct();
    }

    private static Condition<NextInvoiceGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getInvoiceNumber()) &&
                condition.apply(criteria.getIssueDate()) &&
                condition.apply(criteria.getDueDate()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NextInvoiceGammaCriteria> copyFiltersAre(
        NextInvoiceGammaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getInvoiceNumber(), copy.getInvoiceNumber()) &&
                condition.apply(criteria.getIssueDate(), copy.getIssueDate()) &&
                condition.apply(criteria.getDueDate(), copy.getDueDate()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
