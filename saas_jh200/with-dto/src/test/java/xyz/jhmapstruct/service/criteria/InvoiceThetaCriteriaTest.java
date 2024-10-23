package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceThetaCriteriaTest {

    @Test
    void newInvoiceThetaCriteriaHasAllFiltersNullTest() {
        var invoiceThetaCriteria = new InvoiceThetaCriteria();
        assertThat(invoiceThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void invoiceThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceThetaCriteria = new InvoiceThetaCriteria();

        setAllFilters(invoiceThetaCriteria);

        assertThat(invoiceThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void invoiceThetaCriteriaCopyCreatesNullFilterTest() {
        var invoiceThetaCriteria = new InvoiceThetaCriteria();
        var copy = invoiceThetaCriteria.copy();

        assertThat(invoiceThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceThetaCriteria)
        );
    }

    @Test
    void invoiceThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceThetaCriteria = new InvoiceThetaCriteria();
        setAllFilters(invoiceThetaCriteria);

        var copy = invoiceThetaCriteria.copy();

        assertThat(invoiceThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceThetaCriteria = new InvoiceThetaCriteria();

        assertThat(invoiceThetaCriteria).hasToString("InvoiceThetaCriteria{}");
    }

    private static void setAllFilters(InvoiceThetaCriteria invoiceThetaCriteria) {
        invoiceThetaCriteria.id();
        invoiceThetaCriteria.invoiceNumber();
        invoiceThetaCriteria.issueDate();
        invoiceThetaCriteria.dueDate();
        invoiceThetaCriteria.amount();
        invoiceThetaCriteria.tenantId();
        invoiceThetaCriteria.distinct();
    }

    private static Condition<InvoiceThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InvoiceThetaCriteria> copyFiltersAre(
        InvoiceThetaCriteria copy,
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
