package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.MiniGamePlayingTimeStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlLeandroPlayingTime} entity. This class is used
 * in {@link ai.realworld.web.rest.AlLeandroPlayingTimeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-leandro-playing-times?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLeandroPlayingTimeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MiniGamePlayingTimeStatus
     */
    public static class MiniGamePlayingTimeStatusFilter extends Filter<MiniGamePlayingTimeStatus> {

        public MiniGamePlayingTimeStatusFilter() {}

        public MiniGamePlayingTimeStatusFilter(MiniGamePlayingTimeStatusFilter filter) {
            super(filter);
        }

        @Override
        public MiniGamePlayingTimeStatusFilter copy() {
            return new MiniGamePlayingTimeStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private MiniGamePlayingTimeStatusFilter status;

    private InstantFilter wonDate;

    private InstantFilter sentAwardToPlayerAt;

    private StringFilter sentAwardToPlayerBy;

    private InstantFilter playerReceivedTheAwardAt;

    private StringFilter playSourceTime;

    private UUIDFilter miniGameId;

    private UUIDFilter userId;

    private UUIDFilter awardId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlLeandroPlayingTimeCriteria() {}

    public AlLeandroPlayingTimeCriteria(AlLeandroPlayingTimeCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(MiniGamePlayingTimeStatusFilter::copy).orElse(null);
        this.wonDate = other.optionalWonDate().map(InstantFilter::copy).orElse(null);
        this.sentAwardToPlayerAt = other.optionalSentAwardToPlayerAt().map(InstantFilter::copy).orElse(null);
        this.sentAwardToPlayerBy = other.optionalSentAwardToPlayerBy().map(StringFilter::copy).orElse(null);
        this.playerReceivedTheAwardAt = other.optionalPlayerReceivedTheAwardAt().map(InstantFilter::copy).orElse(null);
        this.playSourceTime = other.optionalPlaySourceTime().map(StringFilter::copy).orElse(null);
        this.miniGameId = other.optionalMiniGameId().map(UUIDFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(UUIDFilter::copy).orElse(null);
        this.awardId = other.optionalAwardId().map(UUIDFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlLeandroPlayingTimeCriteria copy() {
        return new AlLeandroPlayingTimeCriteria(this);
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

    public MiniGamePlayingTimeStatusFilter getStatus() {
        return status;
    }

    public Optional<MiniGamePlayingTimeStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public MiniGamePlayingTimeStatusFilter status() {
        if (status == null) {
            setStatus(new MiniGamePlayingTimeStatusFilter());
        }
        return status;
    }

    public void setStatus(MiniGamePlayingTimeStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getWonDate() {
        return wonDate;
    }

    public Optional<InstantFilter> optionalWonDate() {
        return Optional.ofNullable(wonDate);
    }

    public InstantFilter wonDate() {
        if (wonDate == null) {
            setWonDate(new InstantFilter());
        }
        return wonDate;
    }

    public void setWonDate(InstantFilter wonDate) {
        this.wonDate = wonDate;
    }

    public InstantFilter getSentAwardToPlayerAt() {
        return sentAwardToPlayerAt;
    }

    public Optional<InstantFilter> optionalSentAwardToPlayerAt() {
        return Optional.ofNullable(sentAwardToPlayerAt);
    }

    public InstantFilter sentAwardToPlayerAt() {
        if (sentAwardToPlayerAt == null) {
            setSentAwardToPlayerAt(new InstantFilter());
        }
        return sentAwardToPlayerAt;
    }

    public void setSentAwardToPlayerAt(InstantFilter sentAwardToPlayerAt) {
        this.sentAwardToPlayerAt = sentAwardToPlayerAt;
    }

    public StringFilter getSentAwardToPlayerBy() {
        return sentAwardToPlayerBy;
    }

    public Optional<StringFilter> optionalSentAwardToPlayerBy() {
        return Optional.ofNullable(sentAwardToPlayerBy);
    }

    public StringFilter sentAwardToPlayerBy() {
        if (sentAwardToPlayerBy == null) {
            setSentAwardToPlayerBy(new StringFilter());
        }
        return sentAwardToPlayerBy;
    }

    public void setSentAwardToPlayerBy(StringFilter sentAwardToPlayerBy) {
        this.sentAwardToPlayerBy = sentAwardToPlayerBy;
    }

    public InstantFilter getPlayerReceivedTheAwardAt() {
        return playerReceivedTheAwardAt;
    }

    public Optional<InstantFilter> optionalPlayerReceivedTheAwardAt() {
        return Optional.ofNullable(playerReceivedTheAwardAt);
    }

    public InstantFilter playerReceivedTheAwardAt() {
        if (playerReceivedTheAwardAt == null) {
            setPlayerReceivedTheAwardAt(new InstantFilter());
        }
        return playerReceivedTheAwardAt;
    }

    public void setPlayerReceivedTheAwardAt(InstantFilter playerReceivedTheAwardAt) {
        this.playerReceivedTheAwardAt = playerReceivedTheAwardAt;
    }

    public StringFilter getPlaySourceTime() {
        return playSourceTime;
    }

    public Optional<StringFilter> optionalPlaySourceTime() {
        return Optional.ofNullable(playSourceTime);
    }

    public StringFilter playSourceTime() {
        if (playSourceTime == null) {
            setPlaySourceTime(new StringFilter());
        }
        return playSourceTime;
    }

    public void setPlaySourceTime(StringFilter playSourceTime) {
        this.playSourceTime = playSourceTime;
    }

    public UUIDFilter getMiniGameId() {
        return miniGameId;
    }

    public Optional<UUIDFilter> optionalMiniGameId() {
        return Optional.ofNullable(miniGameId);
    }

    public UUIDFilter miniGameId() {
        if (miniGameId == null) {
            setMiniGameId(new UUIDFilter());
        }
        return miniGameId;
    }

    public void setMiniGameId(UUIDFilter miniGameId) {
        this.miniGameId = miniGameId;
    }

    public UUIDFilter getUserId() {
        return userId;
    }

    public Optional<UUIDFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public UUIDFilter userId() {
        if (userId == null) {
            setUserId(new UUIDFilter());
        }
        return userId;
    }

    public void setUserId(UUIDFilter userId) {
        this.userId = userId;
    }

    public UUIDFilter getAwardId() {
        return awardId;
    }

    public Optional<UUIDFilter> optionalAwardId() {
        return Optional.ofNullable(awardId);
    }

    public UUIDFilter awardId() {
        if (awardId == null) {
            setAwardId(new UUIDFilter());
        }
        return awardId;
    }

    public void setAwardId(UUIDFilter awardId) {
        this.awardId = awardId;
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
        final AlLeandroPlayingTimeCriteria that = (AlLeandroPlayingTimeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(wonDate, that.wonDate) &&
            Objects.equals(sentAwardToPlayerAt, that.sentAwardToPlayerAt) &&
            Objects.equals(sentAwardToPlayerBy, that.sentAwardToPlayerBy) &&
            Objects.equals(playerReceivedTheAwardAt, that.playerReceivedTheAwardAt) &&
            Objects.equals(playSourceTime, that.playSourceTime) &&
            Objects.equals(miniGameId, that.miniGameId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(awardId, that.awardId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            status,
            wonDate,
            sentAwardToPlayerAt,
            sentAwardToPlayerBy,
            playerReceivedTheAwardAt,
            playSourceTime,
            miniGameId,
            userId,
            awardId,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLeandroPlayingTimeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalWonDate().map(f -> "wonDate=" + f + ", ").orElse("") +
            optionalSentAwardToPlayerAt().map(f -> "sentAwardToPlayerAt=" + f + ", ").orElse("") +
            optionalSentAwardToPlayerBy().map(f -> "sentAwardToPlayerBy=" + f + ", ").orElse("") +
            optionalPlayerReceivedTheAwardAt().map(f -> "playerReceivedTheAwardAt=" + f + ", ").orElse("") +
            optionalPlaySourceTime().map(f -> "playSourceTime=" + f + ", ").orElse("") +
            optionalMiniGameId().map(f -> "miniGameId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalAwardId().map(f -> "awardId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
