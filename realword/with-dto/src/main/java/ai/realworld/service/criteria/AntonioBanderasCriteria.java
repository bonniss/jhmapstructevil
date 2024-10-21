package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AntonioBanderas} entity. This class is used
 * in {@link ai.realworld.web.rest.AntonioBanderasResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /antonio-banderas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AntonioBanderasCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter level;

    private StringFilter code;

    private StringFilter name;

    private StringFilter fullName;

    private StringFilter nativeName;

    private StringFilter officialCode;

    private StringFilter divisionTerm;

    private BooleanFilter isDeleted;

    private LongFilter currentId;

    private LongFilter parentId;

    private LongFilter childrenId;

    private LongFilter antonioBanderasId;

    private Boolean distinct;

    public AntonioBanderasCriteria() {}

    public AntonioBanderasCriteria(AntonioBanderasCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.level = other.optionalLevel().map(IntegerFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.nativeName = other.optionalNativeName().map(StringFilter::copy).orElse(null);
        this.officialCode = other.optionalOfficialCode().map(StringFilter::copy).orElse(null);
        this.divisionTerm = other.optionalDivisionTerm().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.currentId = other.optionalCurrentId().map(LongFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.childrenId = other.optionalChildrenId().map(LongFilter::copy).orElse(null);
        this.antonioBanderasId = other.optionalAntonioBanderasId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AntonioBanderasCriteria copy() {
        return new AntonioBanderasCriteria(this);
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

    public IntegerFilter getLevel() {
        return level;
    }

    public Optional<IntegerFilter> optionalLevel() {
        return Optional.ofNullable(level);
    }

    public IntegerFilter level() {
        if (level == null) {
            setLevel(new IntegerFilter());
        }
        return level;
    }

    public void setLevel(IntegerFilter level) {
        this.level = level;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getNativeName() {
        return nativeName;
    }

    public Optional<StringFilter> optionalNativeName() {
        return Optional.ofNullable(nativeName);
    }

    public StringFilter nativeName() {
        if (nativeName == null) {
            setNativeName(new StringFilter());
        }
        return nativeName;
    }

    public void setNativeName(StringFilter nativeName) {
        this.nativeName = nativeName;
    }

    public StringFilter getOfficialCode() {
        return officialCode;
    }

    public Optional<StringFilter> optionalOfficialCode() {
        return Optional.ofNullable(officialCode);
    }

    public StringFilter officialCode() {
        if (officialCode == null) {
            setOfficialCode(new StringFilter());
        }
        return officialCode;
    }

    public void setOfficialCode(StringFilter officialCode) {
        this.officialCode = officialCode;
    }

    public StringFilter getDivisionTerm() {
        return divisionTerm;
    }

    public Optional<StringFilter> optionalDivisionTerm() {
        return Optional.ofNullable(divisionTerm);
    }

    public StringFilter divisionTerm() {
        if (divisionTerm == null) {
            setDivisionTerm(new StringFilter());
        }
        return divisionTerm;
    }

    public void setDivisionTerm(StringFilter divisionTerm) {
        this.divisionTerm = divisionTerm;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
    }

    public Optional<BooleanFilter> optionalIsDeleted() {
        return Optional.ofNullable(isDeleted);
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            setIsDeleted(new BooleanFilter());
        }
        return isDeleted;
    }

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LongFilter getCurrentId() {
        return currentId;
    }

    public Optional<LongFilter> optionalCurrentId() {
        return Optional.ofNullable(currentId);
    }

    public LongFilter currentId() {
        if (currentId == null) {
            setCurrentId(new LongFilter());
        }
        return currentId;
    }

    public void setCurrentId(LongFilter currentId) {
        this.currentId = currentId;
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

    public LongFilter getChildrenId() {
        return childrenId;
    }

    public Optional<LongFilter> optionalChildrenId() {
        return Optional.ofNullable(childrenId);
    }

    public LongFilter childrenId() {
        if (childrenId == null) {
            setChildrenId(new LongFilter());
        }
        return childrenId;
    }

    public void setChildrenId(LongFilter childrenId) {
        this.childrenId = childrenId;
    }

    public LongFilter getAntonioBanderasId() {
        return antonioBanderasId;
    }

    public Optional<LongFilter> optionalAntonioBanderasId() {
        return Optional.ofNullable(antonioBanderasId);
    }

    public LongFilter antonioBanderasId() {
        if (antonioBanderasId == null) {
            setAntonioBanderasId(new LongFilter());
        }
        return antonioBanderasId;
    }

    public void setAntonioBanderasId(LongFilter antonioBanderasId) {
        this.antonioBanderasId = antonioBanderasId;
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
        final AntonioBanderasCriteria that = (AntonioBanderasCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(level, that.level) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(nativeName, that.nativeName) &&
            Objects.equals(officialCode, that.officialCode) &&
            Objects.equals(divisionTerm, that.divisionTerm) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(currentId, that.currentId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(antonioBanderasId, that.antonioBanderasId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            level,
            code,
            name,
            fullName,
            nativeName,
            officialCode,
            divisionTerm,
            isDeleted,
            currentId,
            parentId,
            childrenId,
            antonioBanderasId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AntonioBanderasCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLevel().map(f -> "level=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalNativeName().map(f -> "nativeName=" + f + ", ").orElse("") +
            optionalOfficialCode().map(f -> "officialCode=" + f + ", ").orElse("") +
            optionalDivisionTerm().map(f -> "divisionTerm=" + f + ", ").orElse("") +
            optionalIsDeleted().map(f -> "isDeleted=" + f + ", ").orElse("") +
            optionalCurrentId().map(f -> "currentId=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalChildrenId().map(f -> "childrenId=" + f + ", ").orElse("") +
            optionalAntonioBanderasId().map(f -> "antonioBanderasId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
