package ai.realworld.repository;

import ai.realworld.domain.AlLadyGaga;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlLadyGaga entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlLadyGagaRepository extends JpaRepository<AlLadyGaga, UUID>, JpaSpecificationExecutor<AlLadyGaga> {}
