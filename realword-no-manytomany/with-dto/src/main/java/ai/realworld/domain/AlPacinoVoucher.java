package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPacinoVoucher.
 */
@Entity
@Table(name = "al_pacino_voucher")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoVoucher implements Serializable {

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
    @JsonIgnoreProperties(value = { "application", "membershipTier", "alVueVueUsage" }, allowSetters = true)
    private AlPacino user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "image", "alVueVueUsage", "application", "conditions" }, allowSetters = true)
    private AlVueVue voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlPacinoVoucher id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSourceTitle() {
        return this.sourceTitle;
    }

    public AlPacinoVoucher sourceTitle(String sourceTitle) {
        this.setSourceTitle(sourceTitle);
        return this;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getSourceUrl() {
        return this.sourceUrl;
    }

    public AlPacinoVoucher sourceUrl(String sourceUrl) {
        this.setSourceUrl(sourceUrl);
        return this;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Instant getCollectedDate() {
        return this.collectedDate;
    }

    public AlPacinoVoucher collectedDate(Instant collectedDate) {
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

    public AlPacinoVoucher user(AlPacino alPacino) {
        this.setUser(alPacino);
        return this;
    }

    public AlVueVue getVoucher() {
        return this.voucher;
    }

    public void setVoucher(AlVueVue alVueVue) {
        this.voucher = alVueVue;
    }

    public AlPacinoVoucher voucher(AlVueVue alVueVue) {
        this.setVoucher(alVueVue);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlPacinoVoucher application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPacinoVoucher)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPacinoVoucher) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoVoucher{" +
            "id=" + getId() +
            ", sourceTitle='" + getSourceTitle() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            ", collectedDate='" + getCollectedDate() + "'" +
            "}";
    }
}
