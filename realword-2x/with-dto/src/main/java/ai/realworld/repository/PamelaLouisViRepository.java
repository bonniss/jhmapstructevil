package ai.realworld.repository;

import ai.realworld.domain.PamelaLouisVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PamelaLouisVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PamelaLouisViRepository extends JpaRepository<PamelaLouisVi, Long>, JpaSpecificationExecutor<PamelaLouisVi> {}
