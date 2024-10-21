package ai.realworld.repository;

import ai.realworld.domain.AlActisoVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlActisoVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlActisoViRepository extends JpaRepository<AlActisoVi, Long>, JpaSpecificationExecutor<AlActisoVi> {}
