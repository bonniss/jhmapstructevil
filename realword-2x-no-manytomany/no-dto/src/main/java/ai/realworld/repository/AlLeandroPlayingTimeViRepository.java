package ai.realworld.repository;

import ai.realworld.domain.AlLeandroPlayingTimeVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlLeandroPlayingTimeVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlLeandroPlayingTimeViRepository
    extends JpaRepository<AlLeandroPlayingTimeVi, UUID>, JpaSpecificationExecutor<AlLeandroPlayingTimeVi> {}
