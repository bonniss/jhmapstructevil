package ai.realworld.domain;

import ai.realworld.domain.enumeration.EcmaScript;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A JohnLennon.
 */
@Entity
@Table(name = "john_lennon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JohnLennon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private EcmaScript provider;

    @Column(name = "provider_app_id")
    private String providerAppId;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(min = 2, max = 160)
    @Column(name = "slug", length = 160, nullable = false, unique = true)
    private String slug;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alProPros", "alProties", "alProProVis", "alProtyVis" }, allowSetters = true)
    private Metaverse logo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "organization", "applications" }, allowSetters = true)
    private OlAlmantinoMilo appManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "addressVi", "applications" }, allowSetters = true)
    private OlMaster organization;

    @ManyToOne(fetch = FetchType.LAZY)
    private Initium jelloInitium;

    @ManyToOne(fetch = FetchType.LAZY)
    private Initium inhouseInitium;

    @ManyToOne(fetch = FetchType.LAZY)
    private InitiumVi jelloInitiumVi;

    @ManyToOne(fetch = FetchType.LAZY)
    private InitiumVi inhouseInitiumVi;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public JohnLennon id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EcmaScript getProvider() {
        return this.provider;
    }

    public JohnLennon provider(EcmaScript provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(EcmaScript provider) {
        this.provider = provider;
    }

    public String getProviderAppId() {
        return this.providerAppId;
    }

    public JohnLennon providerAppId(String providerAppId) {
        this.setProviderAppId(providerAppId);
        return this;
    }

    public void setProviderAppId(String providerAppId) {
        this.providerAppId = providerAppId;
    }

    public String getName() {
        return this.name;
    }

    public JohnLennon name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return this.slug;
    }

    public JohnLennon slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public JohnLennon isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Metaverse getLogo() {
        return this.logo;
    }

    public void setLogo(Metaverse metaverse) {
        this.logo = metaverse;
    }

    public JohnLennon logo(Metaverse metaverse) {
        this.setLogo(metaverse);
        return this;
    }

    public OlAlmantinoMilo getAppManager() {
        return this.appManager;
    }

    public void setAppManager(OlAlmantinoMilo olAlmantinoMilo) {
        this.appManager = olAlmantinoMilo;
    }

    public JohnLennon appManager(OlAlmantinoMilo olAlmantinoMilo) {
        this.setAppManager(olAlmantinoMilo);
        return this;
    }

    public OlMaster getOrganization() {
        return this.organization;
    }

    public void setOrganization(OlMaster olMaster) {
        this.organization = olMaster;
    }

    public JohnLennon organization(OlMaster olMaster) {
        this.setOrganization(olMaster);
        return this;
    }

    public Initium getJelloInitium() {
        return this.jelloInitium;
    }

    public void setJelloInitium(Initium initium) {
        this.jelloInitium = initium;
    }

    public JohnLennon jelloInitium(Initium initium) {
        this.setJelloInitium(initium);
        return this;
    }

    public Initium getInhouseInitium() {
        return this.inhouseInitium;
    }

    public void setInhouseInitium(Initium initium) {
        this.inhouseInitium = initium;
    }

    public JohnLennon inhouseInitium(Initium initium) {
        this.setInhouseInitium(initium);
        return this;
    }

    public InitiumVi getJelloInitiumVi() {
        return this.jelloInitiumVi;
    }

    public void setJelloInitiumVi(InitiumVi initiumVi) {
        this.jelloInitiumVi = initiumVi;
    }

    public JohnLennon jelloInitiumVi(InitiumVi initiumVi) {
        this.setJelloInitiumVi(initiumVi);
        return this;
    }

    public InitiumVi getInhouseInitiumVi() {
        return this.inhouseInitiumVi;
    }

    public void setInhouseInitiumVi(InitiumVi initiumVi) {
        this.inhouseInitiumVi = initiumVi;
    }

    public JohnLennon inhouseInitiumVi(InitiumVi initiumVi) {
        this.setInhouseInitiumVi(initiumVi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JohnLennon)) {
            return false;
        }
        return getId() != null && getId().equals(((JohnLennon) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JohnLennon{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", providerAppId='" + getProviderAppId() + "'" +
            ", name='" + getName() + "'" +
            ", slug='" + getSlug() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            "}";
    }
}
