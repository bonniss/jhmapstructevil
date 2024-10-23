package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextInvoiceSigmaCriteriaTest {

    @Test
    void newNextInvoiceSigmaCriteriaHasAllFiltersNullTest() {
        var nextInvoiceSigmaCriteria = new NextInvoiceSigmaCriteria();
        assertThat(nextInvoiceSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextInvoiceSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextInvoiceSigmaCriteria = new NextInvoiceSigmaCriteria();

        setAllFilters(nextInvoiceSigmaCriteria);

        assertThat(nextInvoiceSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextInvoiceSigmaCriteriaCopyCreatesNullFilterTest() {
        var nextInvoiceSigmaCriteria = new NextInvoiceSigmaCriteria();
        var copy = nextInvoiceSigmaCriteria.copy();

        assertThat(nextInvoiceSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceSigmaCriteria)
        );
    }

    @Test
    void nextInvoiceSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextInvoiceSigmaCriteria = new NextInvoiceSigmaCriteria();
        setAllFilters(nextInvoiceSigmaCriteria);

        var copy = nextInvoiceSigmaCriteria.copy();

        assertThat(nextInvoiceSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextInvoiceSigmaCriteria = new NextInvoiceSigmaCriteria();

        assertThat(nextInvoiceSigmaCriteria).hasToString("NextInvoiceSigmaCriteria{}");
    }

    private static void setAllFilters(NextInvoiceSigmaCriteria nextInvoiceSigmaCriteria) {
        nextInvoiceSigmaCriteria.id();
        nextInvoiceSigmaCriteria.invoiceNumber();
        nextInvoiceSigmaCriteria.issueDate();
        nextInvoiceSigmaCriteria.dueDate();
        nextInvoiceSigmaCriteria.amount();
        nextInvoiceSigmaCriteria.tenantId();
        nextInvoiceSigmaCriteria.distinct();
    }

    private static Condition<NextInvoiceSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextInvoiceSigmaCriteria> copyFiltersAre(
        NextInvoiceSigmaCriteria copy,
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
