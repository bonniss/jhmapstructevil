package ai.realworld.repository;

import ai.realworld.domain.AlBetonamuRelation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlBetonamuRelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlBetonamuRelationRepository
    extends JpaRepository<AlBetonamuRelation, Long>, JpaSpecificationExecutor<AlBetonamuRelation> {}
