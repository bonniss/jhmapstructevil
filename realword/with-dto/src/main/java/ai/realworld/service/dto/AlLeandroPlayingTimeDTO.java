package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.MiniGamePlayingTimeStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlLeandroPlayingTime} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLeandroPlayingTimeDTO implements Serializable {

    private UUID id;

    private MiniGamePlayingTimeStatus status;

    private Instant wonDate;

    private Instant sentAwardToPlayerAt;

    private String sentAwardToPlayerBy;

    private Instant playerReceivedTheAwardAt;

    private String playSourceTime;

    private AlLeandroDTO miniGame;

    private AlPacinoDTO user;

    private AlDesireDTO award;

    private JohnLennonDTO application;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MiniGamePlayingTimeStatus getStatus() {
        return status;
    }

    public void setStatus(MiniGamePlayingTimeStatus status) {
        this.status = status;
    }

    public Instant getWonDate() {
        return wonDate;
    }

    public void setWonDate(Instant wonDate) {
        this.wonDate = wonDate;
    }

    public Instant getSentAwardToPlayerAt() {
        return sentAwardToPlayerAt;
    }

    public void setSentAwardToPlayerAt(Instant sentAwardToPlayerAt) {
        this.sentAwardToPlayerAt = sentAwardToPlayerAt;
    }

    public String getSentAwardToPlayerBy() {
        return sentAwardToPlayerBy;
    }

    public void setSentAwardToPlayerBy(String sentAwardToPlayerBy) {
        this.sentAwardToPlayerBy = sentAwardToPlayerBy;
    }

    public Instant getPlayerReceivedTheAwardAt() {
        return playerReceivedTheAwardAt;
    }

    public void setPlayerReceivedTheAwardAt(Instant playerReceivedTheAwardAt) {
        this.playerReceivedTheAwardAt = playerReceivedTheAwardAt;
    }

    public String getPlaySourceTime() {
        return playSourceTime;
    }

    public void setPlaySourceTime(String playSourceTime) {
        this.playSourceTime = playSourceTime;
    }

    public AlLeandroDTO getMiniGame() {
        return miniGame;
    }

    public void setMiniGame(AlLeandroDTO miniGame) {
        this.miniGame = miniGame;
    }

    public AlPacinoDTO getUser() {
        return user;
    }

    public void setUser(AlPacinoDTO user) {
        this.user = user;
    }

    public AlDesireDTO getAward() {
        return award;
    }

    public void setAward(AlDesireDTO award) {
        this.award = award;
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
        if (!(o instanceof AlLeandroPlayingTimeDTO)) {
            return false;
        }

        AlLeandroPlayingTimeDTO alLeandroPlayingTimeDTO = (AlLeandroPlayingTimeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alLeandroPlayingTimeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLeandroPlayingTimeDTO{" +
            "id='" + getId() + "'" +
            ", status='" + getStatus() + "'" +
            ", wonDate='" + getWonDate() + "'" +
            ", sentAwardToPlayerAt='" + getSentAwardToPlayerAt() + "'" +
            ", sentAwardToPlayerBy='" + getSentAwardToPlayerBy() + "'" +
            ", playerReceivedTheAwardAt='" + getPlayerReceivedTheAwardAt() + "'" +
            ", playSourceTime='" + getPlaySourceTime() + "'" +
            ", miniGame=" + getMiniGame() +
            ", user=" + getUser() +
            ", award=" + getAward() +
            ", application=" + getApplication() +
            "}";
    }
}
