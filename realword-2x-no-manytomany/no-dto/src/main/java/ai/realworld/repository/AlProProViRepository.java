package ai.realworld.repository;

import ai.realworld.domain.AlProProVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlProProVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlProProViRepository extends JpaRepository<AlProProVi, UUID>, JpaSpecificationExecutor<AlProProVi> {}
