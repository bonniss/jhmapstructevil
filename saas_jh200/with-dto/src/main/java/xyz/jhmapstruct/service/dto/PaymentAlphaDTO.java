package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.PaymentAlphaMethod;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.PaymentAlpha} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentAlphaDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Instant paymentDate;

    private PaymentAlphaMethod paymentMethod;

    private MasterTenantDTO tenant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentAlphaMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentAlphaMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentAlphaDTO)) {
            return false;
        }

        PaymentAlphaDTO paymentAlphaDTO = (PaymentAlphaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentAlphaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentAlphaDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", tenant=" + getTenant() +
            "}";
    }
}
