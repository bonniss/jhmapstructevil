package ai.realworld.repository;

import ai.realworld.domain.AlAlexTypeVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlAlexTypeVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlAlexTypeViRepository extends JpaRepository<AlAlexTypeVi, UUID>, JpaSpecificationExecutor<AlAlexTypeVi> {}
