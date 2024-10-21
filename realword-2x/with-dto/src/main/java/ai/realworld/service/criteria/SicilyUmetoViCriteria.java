package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.SitomutaType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.SicilyUmetoVi} entity. This class is used
 * in {@link ai.realworld.web.rest.SicilyUmetoViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sicily-umeto-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SicilyUmetoViCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SitomutaType
     */
    public static class SitomutaTypeFilter extends Filter<SitomutaType> {

        public SitomutaTypeFilter() {}

        public SitomutaTypeFilter(SitomutaTypeFilter filter) {
            super(filter);
        }

        @Override
        public SitomutaTypeFilter copy() {
            return new SitomutaTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private SitomutaTypeFilter type;

    private StringFilter content;

    private Boolean distinct;

    public SicilyUmetoViCriteria() {}

    public SicilyUmetoViCriteria(SicilyUmetoViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(SitomutaTypeFilter::copy).orElse(null);
        this.content = other.optionalContent().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SicilyUmetoViCriteria copy() {
        return new SicilyUmetoViCriteria(this);
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

    public SitomutaTypeFilter getType() {
        return type;
    }

    public Optional<SitomutaTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public SitomutaTypeFilter type() {
        if (type == null) {
            setType(new SitomutaTypeFilter());
        }
        return type;
    }

    public void setType(SitomutaTypeFilter type) {
        this.type = type;
    }

    public StringFilter getContent() {
        return content;
    }

    public Optional<StringFilter> optionalContent() {
        return Optional.ofNullable(content);
    }

    public StringFilter content() {
        if (content == null) {
            setContent(new StringFilter());
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
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
        final SicilyUmetoViCriteria that = (SicilyUmetoViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(content, that.content) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, content, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SicilyUmetoViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalContent().map(f -> "content=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
