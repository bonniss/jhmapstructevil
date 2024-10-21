package ai.realworld.repository;

import ai.realworld.domain.AlAppleVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlAppleVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlAppleViRepository extends JpaRepository<AlAppleVi, UUID>, JpaSpecificationExecutor<AlAppleVi> {}
