package ai.realworld.repository;

import ai.realworld.domain.OlMaster;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OlMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OlMasterRepository extends JpaRepository<OlMaster, UUID>, JpaSpecificationExecutor<OlMaster> {}
