package ai.realworld.domain;

import ai.realworld.domain.enumeration.EcmaScript;
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
 * A OlAlmantinoMilo.
 */
@Entity
@Table(name = "ol_almantino_milo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OlAlmantinoMilo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private EcmaScript provider;

    @NotNull
    @Size(max = 1024)
    @Column(name = "provider_app_manager_id", length = 1024, nullable = false)
    private String providerAppManagerId;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false, unique = true)
    private String name;

    @Size(max = 1024)
    @Column(name = "provider_secret_key", length = 1024)
    private String providerSecretKey;

    @Size(max = 1024)
    @Column(name = "provider_token", length = 1024)
    private String providerToken;

    @Size(max = 1024)
    @Column(name = "provider_refresh_token", length = 1024)
    private String providerRefreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "applications" }, allowSetters = true)
    private OlMaster organization;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appManager")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private Set<JohnLennon> applications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public OlAlmantinoMilo id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EcmaScript getProvider() {
        return this.provider;
    }

    public OlAlmantinoMilo provider(EcmaScript provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(EcmaScript provider) {
        this.provider = provider;
    }

    public String getProviderAppManagerId() {
        return this.providerAppManagerId;
    }

    public OlAlmantinoMilo providerAppManagerId(String providerAppManagerId) {
        this.setProviderAppManagerId(providerAppManagerId);
        return this;
    }

    public void setProviderAppManagerId(String providerAppManagerId) {
        this.providerAppManagerId = providerAppManagerId;
    }

    public String getName() {
        return this.name;
    }

    public OlAlmantinoMilo name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderSecretKey() {
        return this.providerSecretKey;
    }

    public OlAlmantinoMilo providerSecretKey(String providerSecretKey) {
        this.setProviderSecretKey(providerSecretKey);
        return this;
    }

    public void setProviderSecretKey(String providerSecretKey) {
        this.providerSecretKey = providerSecretKey;
    }

    public String getProviderToken() {
        return this.providerToken;
    }

    public OlAlmantinoMilo providerToken(String providerToken) {
        this.setProviderToken(providerToken);
        return this;
    }

    public void setProviderToken(String providerToken) {
        this.providerToken = providerToken;
    }

    public String getProviderRefreshToken() {
        return this.providerRefreshToken;
    }

    public OlAlmantinoMilo providerRefreshToken(String providerRefreshToken) {
        this.setProviderRefreshToken(providerRefreshToken);
        return this;
    }

    public void setProviderRefreshToken(String providerRefreshToken) {
        this.providerRefreshToken = providerRefreshToken;
    }

    public OlMaster getOrganization() {
        return this.organization;
    }

    public void setOrganization(OlMaster olMaster) {
        this.organization = olMaster;
    }

    public OlAlmantinoMilo organization(OlMaster olMaster) {
        this.setOrganization(olMaster);
        return this;
    }

    public Set<JohnLennon> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<JohnLennon> johnLennons) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.setAppManager(null));
        }
        if (johnLennons != null) {
            johnLennons.forEach(i -> i.setAppManager(this));
        }
        this.applications = johnLennons;
    }

    public OlAlmantinoMilo applications(Set<JohnLennon> johnLennons) {
        this.setApplications(johnLennons);
        return this;
    }

    public OlAlmantinoMilo addApplications(JohnLennon johnLennon) {
        this.applications.add(johnLennon);
        johnLennon.setAppManager(this);
        return this;
    }

    public OlAlmantinoMilo removeApplications(JohnLennon johnLennon) {
        this.applications.remove(johnLennon);
        johnLennon.setAppManager(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OlAlmantinoMilo)) {
            return false;
        }
        return getId() != null && getId().equals(((OlAlmantinoMilo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OlAlmantinoMilo{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", providerAppManagerId='" + getProviderAppManagerId() + "'" +
            ", name='" + getName() + "'" +
            ", providerSecretKey='" + getProviderSecretKey() + "'" +
            ", providerToken='" + getProviderToken() + "'" +
            ", providerRefreshToken='" + getProviderRefreshToken() + "'" +
            "}";
    }
}
