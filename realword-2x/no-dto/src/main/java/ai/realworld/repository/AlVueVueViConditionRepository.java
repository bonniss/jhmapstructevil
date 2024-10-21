package ai.realworld.repository;

import ai.realworld.domain.AlVueVueViCondition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlVueVueViCondition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlVueVueViConditionRepository
    extends JpaRepository<AlVueVueViCondition, Long>, JpaSpecificationExecutor<AlVueVueViCondition> {}
