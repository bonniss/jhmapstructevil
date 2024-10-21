package ai.realworld.repository;

import ai.realworld.domain.AlDesireVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlDesireVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlDesireViRepository extends JpaRepository<AlDesireVi, UUID>, JpaSpecificationExecutor<AlDesireVi> {}
