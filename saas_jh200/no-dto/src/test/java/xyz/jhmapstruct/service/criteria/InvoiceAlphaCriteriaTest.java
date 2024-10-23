package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceAlphaCriteriaTest {

    @Test
    void newInvoiceAlphaCriteriaHasAllFiltersNullTest() {
        var invoiceAlphaCriteria = new InvoiceAlphaCriteria();
        assertThat(invoiceAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void invoiceAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceAlphaCriteria = new InvoiceAlphaCriteria();

        setAllFilters(invoiceAlphaCriteria);

        assertThat(invoiceAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void invoiceAlphaCriteriaCopyCreatesNullFilterTest() {
        var invoiceAlphaCriteria = new InvoiceAlphaCriteria();
        var copy = invoiceAlphaCriteria.copy();

        assertThat(invoiceAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceAlphaCriteria)
        );
    }

    @Test
    void invoiceAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceAlphaCriteria = new InvoiceAlphaCriteria();
        setAllFilters(invoiceAlphaCriteria);

        var copy = invoiceAlphaCriteria.copy();

        assertThat(invoiceAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceAlphaCriteria = new InvoiceAlphaCriteria();

        assertThat(invoiceAlphaCriteria).hasToString("InvoiceAlphaCriteria{}");
    }

    private static void setAllFilters(InvoiceAlphaCriteria invoiceAlphaCriteria) {
        invoiceAlphaCriteria.id();
        invoiceAlphaCriteria.invoiceNumber();
        invoiceAlphaCriteria.issueDate();
        invoiceAlphaCriteria.dueDate();
        invoiceAlphaCriteria.amount();
        invoiceAlphaCriteria.tenantId();
        invoiceAlphaCriteria.distinct();
    }

    private static Condition<InvoiceAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InvoiceAlphaCriteria> copyFiltersAre(
        InvoiceAlphaCriteria copy,
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
