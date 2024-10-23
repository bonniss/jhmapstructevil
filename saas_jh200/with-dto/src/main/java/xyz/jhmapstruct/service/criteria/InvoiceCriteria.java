package xyz.jhmapstruct.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link xyz.jhmapstruct.domain.Invoice} entity. This class is used
 * in {@link xyz.jhmapstruct.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter invoiceNumber;

    private InstantFilter issueDate;

    private InstantFilter dueDate;

    private BigDecimalFilter amount;

    private LongFilter tenantId;

    private Boolean distinct;

    public InvoiceCriteria() {}

    public InvoiceCriteria(InvoiceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.invoiceNumber = other.optionalInvoiceNumber().map(StringFilter::copy).orElse(null);
        this.issueDate = other.optionalIssueDate().map(InstantFilter::copy).orElse(null);
        this.dueDate = other.optionalDueDate().map(InstantFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getInvoiceNumber() {
        return invoiceNumber;
    }

    public Optional<StringFilter> optionalInvoiceNumber() {
        return Optional.ofNullable(invoiceNumber);
    }

    public StringFilter invoiceNumber() {
        if (invoiceNumber == null) {
            setInvoiceNumber(new StringFilter());
        }
        return invoiceNumber;
    }

    public void setInvoiceNumber(StringFilter invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public InstantFilter getIssueDate() {
        return issueDate;
    }

    public Optional<InstantFilter> optionalIssueDate() {
        return Optional.ofNullable(issueDate);
    }

    public InstantFilter issueDate() {
        if (issueDate == null) {
            setIssueDate(new InstantFilter());
        }
        return issueDate;
    }

    public void setIssueDate(InstantFilter issueDate) {
        this.issueDate = issueDate;
    }

    public InstantFilter getDueDate() {
        return dueDate;
    }

    public Optional<InstantFilter> optionalDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public InstantFilter dueDate() {
        if (dueDate == null) {
            setDueDate(new InstantFilter());
        }
        return dueDate;
    }

    public void setDueDate(InstantFilter dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public Optional<LongFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public LongFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new LongFilter());
        }
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(invoiceNumber, that.invoiceNumber) &&
            Objects.equals(issueDate, that.issueDate) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, invoiceNumber, issueDate, dueDate, amount, tenantId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalInvoiceNumber().map(f -> "invoiceNumber=" + f + ", ").orElse("") +
            optionalIssueDate().map(f -> "issueDate=" + f + ", ").orElse("") +
            optionalDueDate().map(f -> "dueDate=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
