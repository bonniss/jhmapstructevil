package ai.realworld.domain;

import ai.realworld.domain.enumeration.AlcountSubjectivity;
import ai.realworld.domain.enumeration.NeonEction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlVueVueCondition.
 */
@Entity
@Table(name = "al_vue_vue_condition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlVueVueCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_type")
    private AlcountSubjectivity subjectType;

    @Column(name = "subject")
    private Long subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private NeonEction action;

    @Size(max = 65535)
    @Column(name = "note", length = 65535)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "image", "alVueVueUsage", "application", "conditions" }, allowSetters = true)
    private AlVueVue parent;

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

    public AlVueVueCondition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlcountSubjectivity getSubjectType() {
        return this.subjectType;
    }

    public AlVueVueCondition subjectType(AlcountSubjectivity subjectType) {
        this.setSubjectType(subjectType);
        return this;
    }

    public void setSubjectType(AlcountSubjectivity subjectType) {
        this.subjectType = subjectType;
    }

    public Long getSubject() {
        return this.subject;
    }

    public AlVueVueCondition subject(Long subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

    public NeonEction getAction() {
        return this.action;
    }

    public AlVueVueCondition action(NeonEction action) {
        this.setAction(action);
        return this;
    }

    public void setAction(NeonEction action) {
        this.action = action;
    }

    public String getNote() {
        return this.note;
    }

    public AlVueVueCondition note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AlVueVue getParent() {
        return this.parent;
    }

    public void setParent(AlVueVue alVueVue) {
        this.parent = alVueVue;
    }

    public AlVueVueCondition parent(AlVueVue alVueVue) {
        this.setParent(alVueVue);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlVueVueCondition application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlVueVueCondition)) {
            return false;
        }
        return getId() != null && getId().equals(((AlVueVueCondition) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlVueVueCondition{" +
            "id=" + getId() +
            ", subjectType='" + getSubjectType() + "'" +
            ", subject=" + getSubject() +
            ", action='" + getAction() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
