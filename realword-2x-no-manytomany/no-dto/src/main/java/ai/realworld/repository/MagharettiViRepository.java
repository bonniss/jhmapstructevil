package ai.realworld.repository;

import ai.realworld.domain.MagharettiVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MagharettiVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MagharettiViRepository extends JpaRepository<MagharettiVi, Long>, JpaSpecificationExecutor<MagharettiVi> {}
