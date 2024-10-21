package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.AlcountSubjectivity;
import ai.realworld.domain.enumeration.NeonEction;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlGoreCondition} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlGoreConditionDTO implements Serializable {

    private Long id;

    private AlcountSubjectivity subjectType;

    private Long subject;

    private NeonEction action;

    @Size(max = 65535)
    private String note;

    private AlGoreDTO parent;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlcountSubjectivity getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(AlcountSubjectivity subjectType) {
        this.subjectType = subjectType;
    }

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

    public NeonEction getAction() {
        return action;
    }

    public void setAction(NeonEction action) {
        this.action = action;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AlGoreDTO getParent() {
        return parent;
    }

    public void setParent(AlGoreDTO parent) {
        this.parent = parent;
    }

    public JohnLennonDTO getApplication() {
        return application;
    }

    public void setApplication(JohnLennonDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlGoreConditionDTO)) {
            return false;
        }

        AlGoreConditionDTO alGoreConditionDTO = (AlGoreConditionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alGoreConditionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlGoreConditionDTO{" +
            "id=" + getId() +
            ", subjectType='" + getSubjectType() + "'" +
            ", subject=" + getSubject() +
            ", action='" + getAction() + "'" +
            ", note='" + getNote() + "'" +
            ", parent=" + getParent() +
            ", application=" + getApplication() +
            "}";
    }
}
