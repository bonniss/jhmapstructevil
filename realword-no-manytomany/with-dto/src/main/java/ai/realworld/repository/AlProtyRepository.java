package ai.realworld.repository;

import ai.realworld.domain.AlProty;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlProty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlProtyRepository extends JpaRepository<AlProty, UUID>, JpaSpecificationExecutor<AlProty> {}
