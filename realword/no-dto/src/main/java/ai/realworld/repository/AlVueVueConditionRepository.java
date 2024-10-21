package ai.realworld.repository;

import ai.realworld.domain.AlVueVueCondition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlVueVueCondition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlVueVueConditionRepository extends JpaRepository<AlVueVueCondition, Long>, JpaSpecificationExecutor<AlVueVueCondition> {}
