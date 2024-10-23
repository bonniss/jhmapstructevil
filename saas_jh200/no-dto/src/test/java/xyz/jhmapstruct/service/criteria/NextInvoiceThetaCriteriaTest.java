package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextInvoiceThetaCriteriaTest {

    @Test
    void newNextInvoiceThetaCriteriaHasAllFiltersNullTest() {
        var nextInvoiceThetaCriteria = new NextInvoiceThetaCriteria();
        assertThat(nextInvoiceThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextInvoiceThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextInvoiceThetaCriteria = new NextInvoiceThetaCriteria();

        setAllFilters(nextInvoiceThetaCriteria);

        assertThat(nextInvoiceThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextInvoiceThetaCriteriaCopyCreatesNullFilterTest() {
        var nextInvoiceThetaCriteria = new NextInvoiceThetaCriteria();
        var copy = nextInvoiceThetaCriteria.copy();

        assertThat(nextInvoiceThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceThetaCriteria)
        );
    }

    @Test
    void nextInvoiceThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextInvoiceThetaCriteria = new NextInvoiceThetaCriteria();
        setAllFilters(nextInvoiceThetaCriteria);

        var copy = nextInvoiceThetaCriteria.copy();

        assertThat(nextInvoiceThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextInvoiceThetaCriteria = new NextInvoiceThetaCriteria();

        assertThat(nextInvoiceThetaCriteria).hasToString("NextInvoiceThetaCriteria{}");
    }

    private static void setAllFilters(NextInvoiceThetaCriteria nextInvoiceThetaCriteria) {
        nextInvoiceThetaCriteria.id();
        nextInvoiceThetaCriteria.invoiceNumber();
        nextInvoiceThetaCriteria.issueDate();
        nextInvoiceThetaCriteria.dueDate();
        nextInvoiceThetaCriteria.amount();
        nextInvoiceThetaCriteria.tenantId();
        nextInvoiceThetaCriteria.distinct();
    }

    private static Condition<NextInvoiceThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextInvoiceThetaCriteria> copyFiltersAre(
        NextInvoiceThetaCriteria copy,
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
