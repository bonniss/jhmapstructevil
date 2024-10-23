package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceGammaCriteriaTest {

    @Test
    void newInvoiceGammaCriteriaHasAllFiltersNullTest() {
        var invoiceGammaCriteria = new InvoiceGammaCriteria();
        assertThat(invoiceGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void invoiceGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceGammaCriteria = new InvoiceGammaCriteria();

        setAllFilters(invoiceGammaCriteria);

        assertThat(invoiceGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void invoiceGammaCriteriaCopyCreatesNullFilterTest() {
        var invoiceGammaCriteria = new InvoiceGammaCriteria();
        var copy = invoiceGammaCriteria.copy();

        assertThat(invoiceGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceGammaCriteria)
        );
    }

    @Test
    void invoiceGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceGammaCriteria = new InvoiceGammaCriteria();
        setAllFilters(invoiceGammaCriteria);

        var copy = invoiceGammaCriteria.copy();

        assertThat(invoiceGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceGammaCriteria = new InvoiceGammaCriteria();

        assertThat(invoiceGammaCriteria).hasToString("InvoiceGammaCriteria{}");
    }

    private static void setAllFilters(InvoiceGammaCriteria invoiceGammaCriteria) {
        invoiceGammaCriteria.id();
        invoiceGammaCriteria.invoiceNumber();
        invoiceGammaCriteria.issueDate();
        invoiceGammaCriteria.dueDate();
        invoiceGammaCriteria.amount();
        invoiceGammaCriteria.tenantId();
        invoiceGammaCriteria.distinct();
    }

    private static Condition<InvoiceGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InvoiceGammaCriteria> copyFiltersAre(
        InvoiceGammaCriteria copy,
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
