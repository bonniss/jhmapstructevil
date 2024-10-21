package ai.realworld.domain;

import ai.realworld.domain.enumeration.EeriePointSource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPacinoPointHistoryVi.
 */
@Entity
@Table(name = "al_pacino_point_history_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoPointHistoryVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private EeriePointSource source;

    @Column(name = "associated_id")
    private String associatedId;

    @Column(name = "point_amount")
    private Integer pointAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "application", "membershipTier", "alVueVueUsage", "membershipTierVi", "alVueVueViUsage" },
        allowSetters = true
    )
    private AlPacino customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlPacinoPointHistoryVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EeriePointSource getSource() {
        return this.source;
    }

    public AlPacinoPointHistoryVi source(EeriePointSource source) {
        this.setSource(source);
        return this;
    }

    public void setSource(EeriePointSource source) {
        this.source = source;
    }

    public String getAssociatedId() {
        return this.associatedId;
    }

    public AlPacinoPointHistoryVi associatedId(String associatedId) {
        this.setAssociatedId(associatedId);
        return this;
    }

    public void setAssociatedId(String associatedId) {
        this.associatedId = associatedId;
    }

    public Integer getPointAmount() {
        return this.pointAmount;
    }

    public AlPacinoPointHistoryVi pointAmount(Integer pointAmount) {
        this.setPointAmount(pointAmount);
        return this;
    }

    public void setPointAmount(Integer pointAmount) {
        this.pointAmount = pointAmount;
    }

    public AlPacino getCustomer() {
        return this.customer;
    }

    public void setCustomer(AlPacino alPacino) {
        this.customer = alPacino;
    }

    public AlPacinoPointHistoryVi customer(AlPacino alPacino) {
        this.setCustomer(alPacino);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlPacinoPointHistoryVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPacinoPointHistoryVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPacinoPointHistoryVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoPointHistoryVi{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", associatedId='" + getAssociatedId() + "'" +
            ", pointAmount=" + getPointAmount() +
            "}";
    }
}
