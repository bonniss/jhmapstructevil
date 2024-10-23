package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceMiMiCriteriaTest {

    @Test
    void newInvoiceMiMiCriteriaHasAllFiltersNullTest() {
        var invoiceMiMiCriteria = new InvoiceMiMiCriteria();
        assertThat(invoiceMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void invoiceMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceMiMiCriteria = new InvoiceMiMiCriteria();

        setAllFilters(invoiceMiMiCriteria);

        assertThat(invoiceMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void invoiceMiMiCriteriaCopyCreatesNullFilterTest() {
        var invoiceMiMiCriteria = new InvoiceMiMiCriteria();
        var copy = invoiceMiMiCriteria.copy();

        assertThat(invoiceMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceMiMiCriteria)
        );
    }

    @Test
    void invoiceMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceMiMiCriteria = new InvoiceMiMiCriteria();
        setAllFilters(invoiceMiMiCriteria);

        var copy = invoiceMiMiCriteria.copy();

        assertThat(invoiceMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceMiMiCriteria = new InvoiceMiMiCriteria();

        assertThat(invoiceMiMiCriteria).hasToString("InvoiceMiMiCriteria{}");
    }

    private static void setAllFilters(InvoiceMiMiCriteria invoiceMiMiCriteria) {
        invoiceMiMiCriteria.id();
        invoiceMiMiCriteria.invoiceNumber();
        invoiceMiMiCriteria.issueDate();
        invoiceMiMiCriteria.dueDate();
        invoiceMiMiCriteria.amount();
        invoiceMiMiCriteria.tenantId();
        invoiceMiMiCriteria.distinct();
    }

    private static Condition<InvoiceMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InvoiceMiMiCriteria> copyFiltersAre(InvoiceMiMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
