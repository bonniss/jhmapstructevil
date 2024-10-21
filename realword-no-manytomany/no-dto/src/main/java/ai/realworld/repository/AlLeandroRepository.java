package ai.realworld.repository;

import ai.realworld.domain.AlLeandro;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlLeandro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlLeandroRepository extends JpaRepository<AlLeandro, UUID>, JpaSpecificationExecutor<AlLeandro> {}
