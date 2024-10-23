package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceSigmaCriteriaTest {

    @Test
    void newInvoiceSigmaCriteriaHasAllFiltersNullTest() {
        var invoiceSigmaCriteria = new InvoiceSigmaCriteria();
        assertThat(invoiceSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void invoiceSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceSigmaCriteria = new InvoiceSigmaCriteria();

        setAllFilters(invoiceSigmaCriteria);

        assertThat(invoiceSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void invoiceSigmaCriteriaCopyCreatesNullFilterTest() {
        var invoiceSigmaCriteria = new InvoiceSigmaCriteria();
        var copy = invoiceSigmaCriteria.copy();

        assertThat(invoiceSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceSigmaCriteria)
        );
    }

    @Test
    void invoiceSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceSigmaCriteria = new InvoiceSigmaCriteria();
        setAllFilters(invoiceSigmaCriteria);

        var copy = invoiceSigmaCriteria.copy();

        assertThat(invoiceSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceSigmaCriteria = new InvoiceSigmaCriteria();

        assertThat(invoiceSigmaCriteria).hasToString("InvoiceSigmaCriteria{}");
    }

    private static void setAllFilters(InvoiceSigmaCriteria invoiceSigmaCriteria) {
        invoiceSigmaCriteria.id();
        invoiceSigmaCriteria.invoiceNumber();
        invoiceSigmaCriteria.issueDate();
        invoiceSigmaCriteria.dueDate();
        invoiceSigmaCriteria.amount();
        invoiceSigmaCriteria.tenantId();
        invoiceSigmaCriteria.distinct();
    }

    private static Condition<InvoiceSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InvoiceSigmaCriteria> copyFiltersAre(
        InvoiceSigmaCriteria copy,
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
