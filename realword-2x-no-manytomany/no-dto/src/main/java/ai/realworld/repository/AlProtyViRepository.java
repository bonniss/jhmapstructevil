package ai.realworld.repository;

import ai.realworld.domain.AlProtyVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlProtyVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlProtyViRepository extends JpaRepository<AlProtyVi, UUID>, JpaSpecificationExecutor<AlProtyVi> {}
