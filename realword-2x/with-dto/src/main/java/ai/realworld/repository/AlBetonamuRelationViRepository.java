package ai.realworld.repository;

import ai.realworld.domain.AlBetonamuRelationVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlBetonamuRelationVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlBetonamuRelationViRepository
    extends JpaRepository<AlBetonamuRelationVi, Long>, JpaSpecificationExecutor<AlBetonamuRelationVi> {}
