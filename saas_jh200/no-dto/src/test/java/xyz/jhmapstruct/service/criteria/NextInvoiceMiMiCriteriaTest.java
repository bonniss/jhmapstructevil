package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextInvoiceMiMiCriteriaTest {

    @Test
    void newNextInvoiceMiMiCriteriaHasAllFiltersNullTest() {
        var nextInvoiceMiMiCriteria = new NextInvoiceMiMiCriteria();
        assertThat(nextInvoiceMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextInvoiceMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextInvoiceMiMiCriteria = new NextInvoiceMiMiCriteria();

        setAllFilters(nextInvoiceMiMiCriteria);

        assertThat(nextInvoiceMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextInvoiceMiMiCriteriaCopyCreatesNullFilterTest() {
        var nextInvoiceMiMiCriteria = new NextInvoiceMiMiCriteria();
        var copy = nextInvoiceMiMiCriteria.copy();

        assertThat(nextInvoiceMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceMiMiCriteria)
        );
    }

    @Test
    void nextInvoiceMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextInvoiceMiMiCriteria = new NextInvoiceMiMiCriteria();
        setAllFilters(nextInvoiceMiMiCriteria);

        var copy = nextInvoiceMiMiCriteria.copy();

        assertThat(nextInvoiceMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextInvoiceMiMiCriteria = new NextInvoiceMiMiCriteria();

        assertThat(nextInvoiceMiMiCriteria).hasToString("NextInvoiceMiMiCriteria{}");
    }

    private static void setAllFilters(NextInvoiceMiMiCriteria nextInvoiceMiMiCriteria) {
        nextInvoiceMiMiCriteria.id();
        nextInvoiceMiMiCriteria.invoiceNumber();
        nextInvoiceMiMiCriteria.issueDate();
        nextInvoiceMiMiCriteria.dueDate();
        nextInvoiceMiMiCriteria.amount();
        nextInvoiceMiMiCriteria.tenantId();
        nextInvoiceMiMiCriteria.distinct();
    }

    private static Condition<NextInvoiceMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextInvoiceMiMiCriteria> copyFiltersAre(
        NextInvoiceMiMiCriteria copy,
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
