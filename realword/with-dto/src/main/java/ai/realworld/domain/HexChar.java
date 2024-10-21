package ai.realworld.domain;

import ai.realworld.domain.enumeration.TyrantSex;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HexChar.
 */
@Entity
@Table(name = "hex_char")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HexChar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "dob")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private TyrantSex gender;

    @Column(name = "phone")
    private String phone;

    @Size(max = 10485760)
    @Column(name = "bio_heitiga", length = 10485760)
    private String bioHeitiga;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private HashRoss role;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HexChar id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public HexChar dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public TyrantSex getGender() {
        return this.gender;
    }

    public HexChar gender(TyrantSex gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(TyrantSex gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public HexChar phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBioHeitiga() {
        return this.bioHeitiga;
    }

    public HexChar bioHeitiga(String bioHeitiga) {
        this.setBioHeitiga(bioHeitiga);
        return this;
    }

    public void setBioHeitiga(String bioHeitiga) {
        this.bioHeitiga = bioHeitiga;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public HexChar isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public HexChar internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public HashRoss getRole() {
        return this.role;
    }

    public void setRole(HashRoss hashRoss) {
        this.role = hashRoss;
    }

    public HexChar role(HashRoss hashRoss) {
        this.setRole(hashRoss);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HexChar)) {
            return false;
        }
        return getId() != null && getId().equals(((HexChar) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HexChar{" +
            "id=" + getId() +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", bioHeitiga='" + getBioHeitiga() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            "}";
    }
}
