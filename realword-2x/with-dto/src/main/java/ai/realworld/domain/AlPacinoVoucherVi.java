package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPacinoVoucherVi.
 */
@Entity
@Table(name = "al_pacino_voucher_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoVoucherVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "source_title")
    private String sourceTitle;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "collected_date")
    private Instant collectedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "application", "membershipTier", "alVueVueUsage", "membershipTierVi", "alVueVueViUsage" },
        allowSetters = true
    )
    private AlPacino user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "image", "alVueVueViUsage", "application", "conditions" }, allowSetters = true)
    private AlVueVueVi voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlPacinoVoucherVi id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSourceTitle() {
        return this.sourceTitle;
    }

    public AlPacinoVoucherVi sourceTitle(String sourceTitle) {
        this.setSourceTitle(sourceTitle);
        return this;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getSourceUrl() {
        return this.sourceUrl;
    }

    public AlPacinoVoucherVi sourceUrl(String sourceUrl) {
        this.setSourceUrl(sourceUrl);
        return this;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Instant getCollectedDate() {
        return this.collectedDate;
    }

    public AlPacinoVoucherVi collectedDate(Instant collectedDate) {
        this.setCollectedDate(collectedDate);
        return this;
    }

    public void setCollectedDate(Instant collectedDate) {
        this.collectedDate = collectedDate;
    }

    public AlPacino getUser() {
        return this.user;
    }

    public void setUser(AlPacino alPacino) {
        this.user = alPacino;
    }

    public AlPacinoVoucherVi user(AlPacino alPacino) {
        this.setUser(alPacino);
        return this;
    }

    public AlVueVueVi getVoucher() {
        return this.voucher;
    }

    public void setVoucher(AlVueVueVi alVueVueVi) {
        this.voucher = alVueVueVi;
    }

    public AlPacinoVoucherVi voucher(AlVueVueVi alVueVueVi) {
        this.setVoucher(alVueVueVi);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlPacinoVoucherVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPacinoVoucherVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPacinoVoucherVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoVoucherVi{" +
            "id=" + getId() +
            ", sourceTitle='" + getSourceTitle() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            ", collectedDate='" + getCollectedDate() + "'" +
            "}";
    }
}
