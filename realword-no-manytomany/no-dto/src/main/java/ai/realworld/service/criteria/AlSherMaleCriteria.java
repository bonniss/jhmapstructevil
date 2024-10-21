package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.AlphaSourceType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlSherMale} entity. This class is used
 * in {@link ai.realworld.web.rest.AlSherMaleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-sher-males?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlSherMaleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AlphaSourceType
     */
    public static class AlphaSourceTypeFilter extends Filter<AlphaSourceType> {

        public AlphaSourceTypeFilter() {}

        public AlphaSourceTypeFilter(AlphaSourceTypeFilter filter) {
            super(filter);
        }

        @Override
        public AlphaSourceTypeFilter copy() {
            return new AlphaSourceTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private AlphaSourceTypeFilter dataSourceMappingType;

    private StringFilter keyword;

    private StringFilter property;

    private StringFilter title;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlSherMaleCriteria() {}

    public AlSherMaleCriteria(AlSherMaleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataSourceMappingType = other.optionalDataSourceMappingType().map(AlphaSourceTypeFilter::copy).orElse(null);
        this.keyword = other.optionalKeyword().map(StringFilter::copy).orElse(null);
        this.property = other.optionalProperty().map(StringFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlSherMaleCriteria copy() {
        return new AlSherMaleCriteria(this);
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

    public AlphaSourceTypeFilter getDataSourceMappingType() {
        return dataSourceMappingType;
    }

    public Optional<AlphaSourceTypeFilter> optionalDataSourceMappingType() {
        return Optional.ofNullable(dataSourceMappingType);
    }

    public AlphaSourceTypeFilter dataSourceMappingType() {
        if (dataSourceMappingType == null) {
            setDataSourceMappingType(new AlphaSourceTypeFilter());
        }
        return dataSourceMappingType;
    }

    public void setDataSourceMappingType(AlphaSourceTypeFilter dataSourceMappingType) {
        this.dataSourceMappingType = dataSourceMappingType;
    }

    public StringFilter getKeyword() {
        return keyword;
    }

    public Optional<StringFilter> optionalKeyword() {
        return Optional.ofNullable(keyword);
    }

    public StringFilter keyword() {
        if (keyword == null) {
            setKeyword(new StringFilter());
        }
        return keyword;
    }

    public void setKeyword(StringFilter keyword) {
        this.keyword = keyword;
    }

    public StringFilter getProperty() {
        return property;
    }

    public Optional<StringFilter> optionalProperty() {
        return Optional.ofNullable(property);
    }

    public StringFilter property() {
        if (property == null) {
            setProperty(new StringFilter());
        }
        return property;
    }

    public void setProperty(StringFilter property) {
        this.property = property;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
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
        final AlSherMaleCriteria that = (AlSherMaleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataSourceMappingType, that.dataSourceMappingType) &&
            Objects.equals(keyword, that.keyword) &&
            Objects.equals(property, that.property) &&
            Objects.equals(title, that.title) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataSourceMappingType, keyword, property, title, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlSherMaleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataSourceMappingType().map(f -> "dataSourceMappingType=" + f + ", ").orElse("") +
            optionalKeyword().map(f -> "keyword=" + f + ", ").orElse("") +
            optionalProperty().map(f -> "property=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
