package ai.realworld.repository;

import ai.realworld.domain.AlMemTierVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlMemTierVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlMemTierViRepository extends JpaRepository<AlMemTierVi, Long>, JpaSpecificationExecutor<AlMemTierVi> {}
