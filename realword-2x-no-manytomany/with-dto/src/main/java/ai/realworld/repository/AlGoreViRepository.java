package ai.realworld.repository;

import ai.realworld.domain.AlGoreVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlGoreVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlGoreViRepository extends JpaRepository<AlGoreVi, Long>, JpaSpecificationExecutor<AlGoreVi> {}
