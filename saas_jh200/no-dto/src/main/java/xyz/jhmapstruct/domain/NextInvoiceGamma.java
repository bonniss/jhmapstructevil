package xyz.jhmapstruct.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NextInvoiceGamma.
 */
@Entity
@Table(name = "next_invoice_gamma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextInvoiceGamma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @NotNull
    @Column(name = "issue_date", nullable = false)
    private Instant issueDate;

    @Column(name = "due_date")
    private Instant dueDate;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextInvoiceGamma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public NextInvoiceGamma invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Instant getIssueDate() {
        return this.issueDate;
    }

    public NextInvoiceGamma issueDate(Instant issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public NextInvoiceGamma dueDate(Instant dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public NextInvoiceGamma amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public NextInvoiceGamma tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextInvoiceGamma)) {
            return false;
        }
        return getId() != null && getId().equals(((NextInvoiceGamma) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextInvoiceGamma{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}