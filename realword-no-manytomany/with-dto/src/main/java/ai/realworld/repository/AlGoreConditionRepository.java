package ai.realworld.repository;

import ai.realworld.domain.AlGoreCondition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlGoreCondition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlGoreConditionRepository extends JpaRepository<AlGoreCondition, Long>, JpaSpecificationExecutor<AlGoreCondition> {}
