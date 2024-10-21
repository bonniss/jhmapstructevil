package ai.realworld.repository;

import ai.realworld.domain.AlGoreConditionVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlGoreConditionVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlGoreConditionViRepository extends JpaRepository<AlGoreConditionVi, Long>, JpaSpecificationExecutor<AlGoreConditionVi> {}
