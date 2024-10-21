package ai.realworld.repository;

import ai.realworld.domain.AlLeandroPlayingTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlLeandroPlayingTime entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlLeandroPlayingTimeRepository
    extends JpaRepository<AlLeandroPlayingTime, UUID>, JpaSpecificationExecutor<AlLeandroPlayingTime> {}
