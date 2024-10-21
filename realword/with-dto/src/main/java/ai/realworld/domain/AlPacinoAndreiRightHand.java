package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPacinoAndreiRightHand.
 */
@Entity
@Table(name = "al_pacino_andrei_right_hand")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoAndreiRightHand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "is_default")
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "application", "membershipTier", "alVueVueUsage" }, allowSetters = true)
    private AlPacino user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "country", "province", "district", "ward" }, allowSetters = true)
    private AndreiRightHand address;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlPacinoAndreiRightHand id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlPacinoAndreiRightHand name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public AlPacinoAndreiRightHand isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public AlPacino getUser() {
        return this.user;
    }

    public void setUser(AlPacino alPacino) {
        this.user = alPacino;
    }

    public AlPacinoAndreiRightHand user(AlPacino alPacino) {
        this.setUser(alPacino);
        return this;
    }

    public AndreiRightHand getAddress() {
        return this.address;
    }

    public void setAddress(AndreiRightHand andreiRightHand) {
        this.address = andreiRightHand;
    }

    public AlPacinoAndreiRightHand address(AndreiRightHand andreiRightHand) {
        this.setAddress(andreiRightHand);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPacinoAndreiRightHand)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPacinoAndreiRightHand) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoAndreiRightHand{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            "}";
    }
}
