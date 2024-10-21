package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlAlexTypeVi.
 */
@Entity
@Table(name = "al_alex_type_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlAlexTypeVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "can_do_retail")
    private Boolean canDoRetail;

    @Column(name = "is_org_division")
    private Boolean isOrgDivision;

    @Size(max = 10485760)
    @Column(name = "config_jason", length = 10485760)
    private String configJason;

    @Column(name = "tree_depth")
    private Integer treeDepth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "supplier", "customer", "application", "discounts" }, allowSetters = true)
    private Set<AlBetonamuRelationVi> asSuppliers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "supplier", "customer", "application", "discounts" }, allowSetters = true)
    private Set<AlBetonamuRelationVi> asCustomers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agencyType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "address", "agencyType", "logo", "application", "agents" }, allowSetters = true)
    private Set<AlAppleVi> agencies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlAlexTypeVi id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlAlexTypeVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public AlAlexTypeVi description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCanDoRetail() {
        return this.canDoRetail;
    }

    public AlAlexTypeVi canDoRetail(Boolean canDoRetail) {
        this.setCanDoRetail(canDoRetail);
        return this;
    }

    public void setCanDoRetail(Boolean canDoRetail) {
        this.canDoRetail = canDoRetail;
    }

    public Boolean getIsOrgDivision() {
        return this.isOrgDivision;
    }

    public AlAlexTypeVi isOrgDivision(Boolean isOrgDivision) {
        this.setIsOrgDivision(isOrgDivision);
        return this;
    }

    public void setIsOrgDivision(Boolean isOrgDivision) {
        this.isOrgDivision = isOrgDivision;
    }

    public String getConfigJason() {
        return this.configJason;
    }

    public AlAlexTypeVi configJason(String configJason) {
        this.setConfigJason(configJason);
        return this;
    }

    public void setConfigJason(String configJason) {
        this.configJason = configJason;
    }

    public Integer getTreeDepth() {
        return this.treeDepth;
    }

    public AlAlexTypeVi treeDepth(Integer treeDepth) {
        this.setTreeDepth(treeDepth);
        return this;
    }

    public void setTreeDepth(Integer treeDepth) {
        this.treeDepth = treeDepth;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlAlexTypeVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlBetonamuRelationVi> getAsSuppliers() {
        return this.asSuppliers;
    }

    public void setAsSuppliers(Set<AlBetonamuRelationVi> alBetonamuRelationVis) {
        if (this.asSuppliers != null) {
            this.asSuppliers.forEach(i -> i.setSupplier(null));
        }
        if (alBetonamuRelationVis != null) {
            alBetonamuRelationVis.forEach(i -> i.setSupplier(this));
        }
        this.asSuppliers = alBetonamuRelationVis;
    }

    public AlAlexTypeVi asSuppliers(Set<AlBetonamuRelationVi> alBetonamuRelationVis) {
        this.setAsSuppliers(alBetonamuRelationVis);
        return this;
    }

    public AlAlexTypeVi addAsSupplier(AlBetonamuRelationVi alBetonamuRelationVi) {
        this.asSuppliers.add(alBetonamuRelationVi);
        alBetonamuRelationVi.setSupplier(this);
        return this;
    }

    public AlAlexTypeVi removeAsSupplier(AlBetonamuRelationVi alBetonamuRelationVi) {
        this.asSuppliers.remove(alBetonamuRelationVi);
        alBetonamuRelationVi.setSupplier(null);
        return this;
    }

    public Set<AlBetonamuRelationVi> getAsCustomers() {
        return this.asCustomers;
    }

    public void setAsCustomers(Set<AlBetonamuRelationVi> alBetonamuRelationVis) {
        if (this.asCustomers != null) {
            this.asCustomers.forEach(i -> i.setCustomer(null));
        }
        if (alBetonamuRelationVis != null) {
            alBetonamuRelationVis.forEach(i -> i.setCustomer(this));
        }
        this.asCustomers = alBetonamuRelationVis;
    }

    public AlAlexTypeVi asCustomers(Set<AlBetonamuRelationVi> alBetonamuRelationVis) {
        this.setAsCustomers(alBetonamuRelationVis);
        return this;
    }

    public AlAlexTypeVi addAsCustomer(AlBetonamuRelationVi alBetonamuRelationVi) {
        this.asCustomers.add(alBetonamuRelationVi);
        alBetonamuRelationVi.setCustomer(this);
        return this;
    }

    public AlAlexTypeVi removeAsCustomer(AlBetonamuRelationVi alBetonamuRelationVi) {
        this.asCustomers.remove(alBetonamuRelationVi);
        alBetonamuRelationVi.setCustomer(null);
        return this;
    }

    public Set<AlAppleVi> getAgencies() {
        return this.agencies;
    }

    public void setAgencies(Set<AlAppleVi> alAppleVis) {
        if (this.agencies != null) {
            this.agencies.forEach(i -> i.setAgencyType(null));
        }
        if (alAppleVis != null) {
            alAppleVis.forEach(i -> i.setAgencyType(this));
        }
        this.agencies = alAppleVis;
    }

    public AlAlexTypeVi agencies(Set<AlAppleVi> alAppleVis) {
        this.setAgencies(alAppleVis);
        return this;
    }

    public AlAlexTypeVi addAgencies(AlAppleVi alAppleVi) {
        this.agencies.add(alAppleVi);
        alAppleVi.setAgencyType(this);
        return this;
    }

    public AlAlexTypeVi removeAgencies(AlAppleVi alAppleVi) {
        this.agencies.remove(alAppleVi);
        alAppleVi.setAgencyType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlAlexTypeVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlAlexTypeVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlAlexTypeVi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", canDoRetail='" + getCanDoRetail() + "'" +
            ", isOrgDivision='" + getIsOrgDivision() + "'" +
            ", configJason='" + getConfigJason() + "'" +
            ", treeDepth=" + getTreeDepth() +
            "}";
    }
}
