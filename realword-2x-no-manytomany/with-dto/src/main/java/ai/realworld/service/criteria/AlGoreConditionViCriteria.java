package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.AlcountSubjectivity;
import ai.realworld.domain.enumeration.NeonEction;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlGoreConditionVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlGoreConditionViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-gore-condition-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlGoreConditionViCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AlcountSubjectivity
     */
    public static class AlcountSubjectivityFilter extends Filter<AlcountSubjectivity> {

        public AlcountSubjectivityFilter() {}

        public AlcountSubjectivityFilter(AlcountSubjectivityFilter filter) {
            super(filter);
        }

        @Override
        public AlcountSubjectivityFilter copy() {
            return new AlcountSubjectivityFilter(this);
        }
    }

    /**
     * Class for filtering NeonEction
     */
    public static class NeonEctionFilter extends Filter<NeonEction> {

        public NeonEctionFilter() {}

        public NeonEctionFilter(NeonEctionFilter filter) {
            super(filter);
        }

        @Override
        public NeonEctionFilter copy() {
            return new NeonEctionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private AlcountSubjectivityFilter subjectType;

    private LongFilter subject;

    private NeonEctionFilter action;

    private StringFilter note;

    private LongFilter parentId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlGoreConditionViCriteria() {}

    public AlGoreConditionViCriteria(AlGoreConditionViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.subjectType = other.optionalSubjectType().map(AlcountSubjectivityFilter::copy).orElse(null);
        this.subject = other.optionalSubject().map(LongFilter::copy).orElse(null);
        this.action = other.optionalAction().map(NeonEctionFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlGoreConditionViCriteria copy() {
        return new AlGoreConditionViCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public AlcountSubjectivityFilter getSubjectType() {
        return subjectType;
    }

    public Optional<AlcountSubjectivityFilter> optionalSubjectType() {
        return Optional.ofNullable(subjectType);
    }

    public AlcountSubjectivityFilter subjectType() {
        if (subjectType == null) {
            setSubjectType(new AlcountSubjectivityFilter());
        }
        return subjectType;
    }

    public void setSubjectType(AlcountSubjectivityFilter subjectType) {
        this.subjectType = subjectType;
    }

    public LongFilter getSubject() {
        return subject;
    }

    public Optional<LongFilter> optionalSubject() {
        return Optional.ofNullable(subject);
    }

    public LongFilter subject() {
        if (subject == null) {
            setSubject(new LongFilter());
        }
        return subject;
    }

    public void setSubject(LongFilter subject) {
        this.subject = subject;
    }

    public NeonEctionFilter getAction() {
        return action;
    }

    public Optional<NeonEctionFilter> optionalAction() {
        return Optional.ofNullable(action);
    }

    public NeonEctionFilter action() {
        if (action == null) {
            setAction(new NeonEctionFilter());
        }
        return action;
    }

    public void setAction(NeonEctionFilter action) {
        this.action = action;
    }

    public StringFilter getNote() {
        return note;
    }

    public Optional<StringFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public StringFilter note() {
        if (note == null) {
            setNote(new StringFilter());
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public Optional<LongFilter> optionalParentId() {
        return Optional.ofNullable(parentId);
    }

    public LongFilter parentId() {
        if (parentId == null) {
            setParentId(new LongFilter());
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public UUIDFilter getApplicationId() {
        return applicationId;
    }

    public Optional<UUIDFilter> optionalApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public UUIDFilter applicationId() {
        if (applicationId == null) {
            setApplicationId(new UUIDFilter());
        }
        return applicationId;
    }

    public void setApplicationId(UUIDFilter applicationId) {
        this.applicationId = applicationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlGoreConditionViCriteria that = (AlGoreConditionViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(subjectType, that.subjectType) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(action, that.action) &&
            Objects.equals(note, that.note) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectType, subject, action, note, parentId, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlGoreConditionViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSubjectType().map(f -> "subjectType=" + f + ", ").orElse("") +
            optionalSubject().map(f -> "subject=" + f + ", ").orElse("") +
            optionalAction().map(f -> "action=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
