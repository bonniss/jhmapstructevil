package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AndreiRightHandVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AndreiRightHandViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /andrei-right-hand-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AndreiRightHandViCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter details;

    private FloatFilter lat;

    private FloatFilter lon;

    private LongFilter countryId;

    private LongFilter provinceId;

    private LongFilter districtId;

    private LongFilter wardId;

    private Boolean distinct;

    public AndreiRightHandViCriteria() {}

    public AndreiRightHandViCriteria(AndreiRightHandViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.details = other.optionalDetails().map(StringFilter::copy).orElse(null);
        this.lat = other.optionalLat().map(FloatFilter::copy).orElse(null);
        this.lon = other.optionalLon().map(FloatFilter::copy).orElse(null);
        this.countryId = other.optionalCountryId().map(LongFilter::copy).orElse(null);
        this.provinceId = other.optionalProvinceId().map(LongFilter::copy).orElse(null);
        this.districtId = other.optionalDistrictId().map(LongFilter::copy).orElse(null);
        this.wardId = other.optionalWardId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AndreiRightHandViCriteria copy() {
        return new AndreiRightHandViCriteria(this);
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

    public StringFilter getDetails() {
        return details;
    }

    public Optional<StringFilter> optionalDetails() {
        return Optional.ofNullable(details);
    }

    public StringFilter details() {
        if (details == null) {
            setDetails(new StringFilter());
        }
        return details;
    }

    public void setDetails(StringFilter details) {
        this.details = details;
    }

    public FloatFilter getLat() {
        return lat;
    }

    public Optional<FloatFilter> optionalLat() {
        return Optional.ofNullable(lat);
    }

    public FloatFilter lat() {
        if (lat == null) {
            setLat(new FloatFilter());
        }
        return lat;
    }

    public void setLat(FloatFilter lat) {
        this.lat = lat;
    }

    public FloatFilter getLon() {
        return lon;
    }

    public Optional<FloatFilter> optionalLon() {
        return Optional.ofNullable(lon);
    }

    public FloatFilter lon() {
        if (lon == null) {
            setLon(new FloatFilter());
        }
        return lon;
    }

    public void setLon(FloatFilter lon) {
        this.lon = lon;
    }

    public LongFilter getCountryId() {
        return countryId;
    }

    public Optional<LongFilter> optionalCountryId() {
        return Optional.ofNullable(countryId);
    }

    public LongFilter countryId() {
        if (countryId == null) {
            setCountryId(new LongFilter());
        }
        return countryId;
    }

    public void setCountryId(LongFilter countryId) {
        this.countryId = countryId;
    }

    public LongFilter getProvinceId() {
        return provinceId;
    }

    public Optional<LongFilter> optionalProvinceId() {
        return Optional.ofNullable(provinceId);
    }

    public LongFilter provinceId() {
        if (provinceId == null) {
            setProvinceId(new LongFilter());
        }
        return provinceId;
    }

    public void setProvinceId(LongFilter provinceId) {
        this.provinceId = provinceId;
    }

    public LongFilter getDistrictId() {
        return districtId;
    }

    public Optional<LongFilter> optionalDistrictId() {
        return Optional.ofNullable(districtId);
    }

    public LongFilter districtId() {
        if (districtId == null) {
            setDistrictId(new LongFilter());
        }
        return districtId;
    }

    public void setDistrictId(LongFilter districtId) {
        this.districtId = districtId;
    }

    public LongFilter getWardId() {
        return wardId;
    }

    public Optional<LongFilter> optionalWardId() {
        return Optional.ofNullable(wardId);
    }

    public LongFilter wardId() {
        if (wardId == null) {
            setWardId(new LongFilter());
        }
        return wardId;
    }

    public void setWardId(LongFilter wardId) {
        this.wardId = wardId;
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
        final AndreiRightHandViCriteria that = (AndreiRightHandViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(details, that.details) &&
            Objects.equals(lat, that.lat) &&
            Objects.equals(lon, that.lon) &&
            Objects.equals(countryId, that.countryId) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(districtId, that.districtId) &&
            Objects.equals(wardId, that.wardId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, details, lat, lon, countryId, provinceId, districtId, wardId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AndreiRightHandViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDetails().map(f -> "details=" + f + ", ").orElse("") +
            optionalLat().map(f -> "lat=" + f + ", ").orElse("") +
            optionalLon().map(f -> "lon=" + f + ", ").orElse("") +
            optionalCountryId().map(f -> "countryId=" + f + ", ").orElse("") +
            optionalProvinceId().map(f -> "provinceId=" + f + ", ").orElse("") +
            optionalDistrictId().map(f -> "districtId=" + f + ", ").orElse("") +
            optionalWardId().map(f -> "wardId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
