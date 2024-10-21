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
 * A AlVueVueViCondition.
 */
@Entity
@Table(name = "al_vue_vue_vi_condition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlVueVueViCondition implements Serializable {

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
    @JsonIgnoreProperties(value = { "image", "alVueVueViUsage", "application", "conditions" }, allowSetters = true)
    private AlVueVueVi parent;

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

    public AlVueVueViCondition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlcountSubjectivity getSubjectType() {
        return this.subjectType;
    }

    public AlVueVueViCondition subjectType(AlcountSubjectivity subjectType) {
        this.setSubjectType(subjectType);
        return this;
    }

    public void setSubjectType(AlcountSubjectivity subjectType) {
        this.subjectType = subjectType;
    }

    public Long getSubject() {
        return this.subject;
    }

    public AlVueVueViCondition subject(Long subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

    public NeonEction getAction() {
        return this.action;
    }

    public AlVueVueViCondition action(NeonEction action) {
        this.setAction(action);
        return this;
    }

    public void setAction(NeonEction action) {
        this.action = action;
    }

    public String getNote() {
        return this.note;
    }

    public AlVueVueViCondition note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AlVueVueVi getParent() {
        return this.parent;
    }

    public void setParent(AlVueVueVi alVueVueVi) {
        this.parent = alVueVueVi;
    }

    public AlVueVueViCondition parent(AlVueVueVi alVueVueVi) {
        this.setParent(alVueVueVi);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlVueVueViCondition application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlVueVueViCondition)) {
            return false;
        }
        return getId() != null && getId().equals(((AlVueVueViCondition) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlVueVueViCondition{" +
            "id=" + getId() +
            ", subjectType='" + getSubjectType() + "'" +
            ", subject=" + getSubject() +
            ", action='" + getAction() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
