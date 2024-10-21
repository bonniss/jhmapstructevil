package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.BenedictType;
import ai.realworld.domain.enumeration.PaulBargainStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlPyuJokerVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlPyuJokerViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-pyu-joker-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPyuJokerViCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BenedictType
     */
    public static class BenedictTypeFilter extends Filter<BenedictType> {

        public BenedictTypeFilter() {}

        public BenedictTypeFilter(BenedictTypeFilter filter) {
            super(filter);
        }

        @Override
        public BenedictTypeFilter copy() {
            return new BenedictTypeFilter(this);
        }
    }

    /**
     * Class for filtering PaulBargainStatus
     */
    public static class PaulBargainStatusFilter extends Filter<PaulBargainStatus> {

        public PaulBargainStatusFilter() {}

        public PaulBargainStatusFilter(PaulBargainStatusFilter filter) {
            super(filter);
        }

        @Override
        public PaulBargainStatusFilter copy() {
            return new PaulBargainStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter bookingNo;

    private StringFilter noteHeitiga;

    private BenedictTypeFilter periodType;

    private InstantFilter fromDate;

    private InstantFilter toDate;

    private InstantFilter checkInDate;

    private InstantFilter checkOutDate;

    private IntegerFilter numberOfAdults;

    private IntegerFilter numberOfPreschoolers;

    private IntegerFilter numberOfChildren;

    private BigDecimalFilter bookingPrice;

    private BigDecimalFilter extraFee;

    private BigDecimalFilter totalPrice;

    private PaulBargainStatusFilter bookingStatus;

    private StringFilter historyRefJason;

    private UUIDFilter customerId;

    private LongFilter personInChargeId;

    private UUIDFilter applicationId;

    private UUIDFilter propertyId;

    private Boolean distinct;

    public AlPyuJokerViCriteria() {}

    public AlPyuJokerViCriteria(AlPyuJokerViCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.bookingNo = other.optionalBookingNo().map(StringFilter::copy).orElse(null);
        this.noteHeitiga = other.optionalNoteHeitiga().map(StringFilter::copy).orElse(null);
        this.periodType = other.optionalPeriodType().map(BenedictTypeFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.toDate = other.optionalToDate().map(InstantFilter::copy).orElse(null);
        this.checkInDate = other.optionalCheckInDate().map(InstantFilter::copy).orElse(null);
        this.checkOutDate = other.optionalCheckOutDate().map(InstantFilter::copy).orElse(null);
        this.numberOfAdults = other.optionalNumberOfAdults().map(IntegerFilter::copy).orElse(null);
        this.numberOfPreschoolers = other.optionalNumberOfPreschoolers().map(IntegerFilter::copy).orElse(null);
        this.numberOfChildren = other.optionalNumberOfChildren().map(IntegerFilter::copy).orElse(null);
        this.bookingPrice = other.optionalBookingPrice().map(BigDecimalFilter::copy).orElse(null);
        this.extraFee = other.optionalExtraFee().map(BigDecimalFilter::copy).orElse(null);
        this.totalPrice = other.optionalTotalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.bookingStatus = other.optionalBookingStatus().map(PaulBargainStatusFilter::copy).orElse(null);
        this.historyRefJason = other.optionalHistoryRefJason().map(StringFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(UUIDFilter::copy).orElse(null);
        this.personInChargeId = other.optionalPersonInChargeId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.propertyId = other.optionalPropertyId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlPyuJokerViCriteria copy() {
        return new AlPyuJokerViCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getBookingNo() {
        return bookingNo;
    }

    public Optional<StringFilter> optionalBookingNo() {
        return Optional.ofNullable(bookingNo);
    }

    public StringFilter bookingNo() {
        if (bookingNo == null) {
            setBookingNo(new StringFilter());
        }
        return bookingNo;
    }

    public void setBookingNo(StringFilter bookingNo) {
        this.bookingNo = bookingNo;
    }

    public StringFilter getNoteHeitiga() {
        return noteHeitiga;
    }

    public Optional<StringFilter> optionalNoteHeitiga() {
        return Optional.ofNullable(noteHeitiga);
    }

    public StringFilter noteHeitiga() {
        if (noteHeitiga == null) {
            setNoteHeitiga(new StringFilter());
        }
        return noteHeitiga;
    }

    public void setNoteHeitiga(StringFilter noteHeitiga) {
        this.noteHeitiga = noteHeitiga;
    }

    public BenedictTypeFilter getPeriodType() {
        return periodType;
    }

    public Optional<BenedictTypeFilter> optionalPeriodType() {
        return Optional.ofNullable(periodType);
    }

    public BenedictTypeFilter periodType() {
        if (periodType == null) {
            setPeriodType(new BenedictTypeFilter());
        }
        return periodType;
    }

    public void setPeriodType(BenedictTypeFilter periodType) {
        this.periodType = periodType;
    }

    public InstantFilter getFromDate() {
        return fromDate;
    }

    public Optional<InstantFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public InstantFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new InstantFilter());
        }
        return fromDate;
    }

    public void setFromDate(InstantFilter fromDate) {
        this.fromDate = fromDate;
    }

    public InstantFilter getToDate() {
        return toDate;
    }

    public Optional<InstantFilter> optionalToDate() {
        return Optional.ofNullable(toDate);
    }

    public InstantFilter toDate() {
        if (toDate == null) {
            setToDate(new InstantFilter());
        }
        return toDate;
    }

    public void setToDate(InstantFilter toDate) {
        this.toDate = toDate;
    }

    public InstantFilter getCheckInDate() {
        return checkInDate;
    }

    public Optional<InstantFilter> optionalCheckInDate() {
        return Optional.ofNullable(checkInDate);
    }

    public InstantFilter checkInDate() {
        if (checkInDate == null) {
            setCheckInDate(new InstantFilter());
        }
        return checkInDate;
    }

    public void setCheckInDate(InstantFilter checkInDate) {
        this.checkInDate = checkInDate;
    }

    public InstantFilter getCheckOutDate() {
        return checkOutDate;
    }

    public Optional<InstantFilter> optionalCheckOutDate() {
        return Optional.ofNullable(checkOutDate);
    }

    public InstantFilter checkOutDate() {
        if (checkOutDate == null) {
            setCheckOutDate(new InstantFilter());
        }
        return checkOutDate;
    }

    public void setCheckOutDate(InstantFilter checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public IntegerFilter getNumberOfAdults() {
        return numberOfAdults;
    }

    public Optional<IntegerFilter> optionalNumberOfAdults() {
        return Optional.ofNullable(numberOfAdults);
    }

    public IntegerFilter numberOfAdults() {
        if (numberOfAdults == null) {
            setNumberOfAdults(new IntegerFilter());
        }
        return numberOfAdults;
    }

    public void setNumberOfAdults(IntegerFilter numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public IntegerFilter getNumberOfPreschoolers() {
        return numberOfPreschoolers;
    }

    public Optional<IntegerFilter> optionalNumberOfPreschoolers() {
        return Optional.ofNullable(numberOfPreschoolers);
    }

    public IntegerFilter numberOfPreschoolers() {
        if (numberOfPreschoolers == null) {
            setNumberOfPreschoolers(new IntegerFilter());
        }
        return numberOfPreschoolers;
    }

    public void setNumberOfPreschoolers(IntegerFilter numberOfPreschoolers) {
        this.numberOfPreschoolers = numberOfPreschoolers;
    }

    public IntegerFilter getNumberOfChildren() {
        return numberOfChildren;
    }

    public Optional<IntegerFilter> optionalNumberOfChildren() {
        return Optional.ofNullable(numberOfChildren);
    }

    public IntegerFilter numberOfChildren() {
        if (numberOfChildren == null) {
            setNumberOfChildren(new IntegerFilter());
        }
        return numberOfChildren;
    }

    public void setNumberOfChildren(IntegerFilter numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public BigDecimalFilter getBookingPrice() {
        return bookingPrice;
    }

    public Optional<BigDecimalFilter> optionalBookingPrice() {
        return Optional.ofNullable(bookingPrice);
    }

    public BigDecimalFilter bookingPrice() {
        if (bookingPrice == null) {
            setBookingPrice(new BigDecimalFilter());
        }
        return bookingPrice;
    }

    public void setBookingPrice(BigDecimalFilter bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public BigDecimalFilter getExtraFee() {
        return extraFee;
    }

    public Optional<BigDecimalFilter> optionalExtraFee() {
        return Optional.ofNullable(extraFee);
    }

    public BigDecimalFilter extraFee() {
        if (extraFee == null) {
            setExtraFee(new BigDecimalFilter());
        }
        return extraFee;
    }

    public void setExtraFee(BigDecimalFilter extraFee) {
        this.extraFee = extraFee;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public Optional<BigDecimalFilter> optionalTotalPrice() {
        return Optional.ofNullable(totalPrice);
    }

    public BigDecimalFilter totalPrice() {
        if (totalPrice == null) {
            setTotalPrice(new BigDecimalFilter());
        }
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PaulBargainStatusFilter getBookingStatus() {
        return bookingStatus;
    }

    public Optional<PaulBargainStatusFilter> optionalBookingStatus() {
        return Optional.ofNullable(bookingStatus);
    }

    public PaulBargainStatusFilter bookingStatus() {
        if (bookingStatus == null) {
            setBookingStatus(new PaulBargainStatusFilter());
        }
        return bookingStatus;
    }

    public void setBookingStatus(PaulBargainStatusFilter bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public StringFilter getHistoryRefJason() {
        return historyRefJason;
    }

    public Optional<StringFilter> optionalHistoryRefJason() {
        return Optional.ofNullable(historyRefJason);
    }

    public StringFilter historyRefJason() {
        if (historyRefJason == null) {
            setHistoryRefJason(new StringFilter());
        }
        return historyRefJason;
    }

    public void setHistoryRefJason(StringFilter historyRefJason) {
        this.historyRefJason = historyRefJason;
    }

    public UUIDFilter getCustomerId() {
        return customerId;
    }

    public Optional<UUIDFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public UUIDFilter customerId() {
        if (customerId == null) {
            setCustomerId(new UUIDFilter());
        }
        return customerId;
    }

    public void setCustomerId(UUIDFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getPersonInChargeId() {
        return personInChargeId;
    }

    public Optional<LongFilter> optionalPersonInChargeId() {
        return Optional.ofNullable(personInChargeId);
    }

    public LongFilter personInChargeId() {
        if (personInChargeId == null) {
            setPersonInChargeId(new LongFilter());
        }
        return personInChargeId;
    }

    public void setPersonInChargeId(LongFilter personInChargeId) {
        this.personInChargeId = personInChargeId;
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

    public UUIDFilter getPropertyId() {
        return propertyId;
    }

    public Optional<UUIDFilter> optionalPropertyId() {
        return Optional.ofNullable(propertyId);
    }

    public UUIDFilter propertyId() {
        if (propertyId == null) {
            setPropertyId(new UUIDFilter());
        }
        return propertyId;
    }

    public void setPropertyId(UUIDFilter propertyId) {
        this.propertyId = propertyId;
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
        final AlPyuJokerViCriteria that = (AlPyuJokerViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bookingNo, that.bookingNo) &&
            Objects.equals(noteHeitiga, that.noteHeitiga) &&
            Objects.equals(periodType, that.periodType) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(toDate, that.toDate) &&
            Objects.equals(checkInDate, that.checkInDate) &&
            Objects.equals(checkOutDate, that.checkOutDate) &&
            Objects.equals(numberOfAdults, that.numberOfAdults) &&
            Objects.equals(numberOfPreschoolers, that.numberOfPreschoolers) &&
            Objects.equals(numberOfChildren, that.numberOfChildren) &&
            Objects.equals(bookingPrice, that.bookingPrice) &&
            Objects.equals(extraFee, that.extraFee) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(bookingStatus, that.bookingStatus) &&
            Objects.equals(historyRefJason, that.historyRefJason) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(personInChargeId, that.personInChargeId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(propertyId, that.propertyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            bookingNo,
            noteHeitiga,
            periodType,
            fromDate,
            toDate,
            checkInDate,
            checkOutDate,
            numberOfAdults,
            numberOfPreschoolers,
            numberOfChildren,
            bookingPrice,
            extraFee,
            totalPrice,
            bookingStatus,
            historyRefJason,
            customerId,
            personInChargeId,
            applicationId,
            propertyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPyuJokerViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBookingNo().map(f -> "bookingNo=" + f + ", ").orElse("") +
            optionalNoteHeitiga().map(f -> "noteHeitiga=" + f + ", ").orElse("") +
            optionalPeriodType().map(f -> "periodType=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalToDate().map(f -> "toDate=" + f + ", ").orElse("") +
            optionalCheckInDate().map(f -> "checkInDate=" + f + ", ").orElse("") +
            optionalCheckOutDate().map(f -> "checkOutDate=" + f + ", ").orElse("") +
            optionalNumberOfAdults().map(f -> "numberOfAdults=" + f + ", ").orElse("") +
            optionalNumberOfPreschoolers().map(f -> "numberOfPreschoolers=" + f + ", ").orElse("") +
            optionalNumberOfChildren().map(f -> "numberOfChildren=" + f + ", ").orElse("") +
            optionalBookingPrice().map(f -> "bookingPrice=" + f + ", ").orElse("") +
            optionalExtraFee().map(f -> "extraFee=" + f + ", ").orElse("") +
            optionalTotalPrice().map(f -> "totalPrice=" + f + ", ").orElse("") +
            optionalBookingStatus().map(f -> "bookingStatus=" + f + ", ").orElse("") +
            optionalHistoryRefJason().map(f -> "historyRefJason=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalPersonInChargeId().map(f -> "personInChargeId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalPropertyId().map(f -> "propertyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
