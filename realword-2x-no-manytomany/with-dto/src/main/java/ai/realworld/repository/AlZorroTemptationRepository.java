package ai.realworld.repository;

import ai.realworld.domain.AlZorroTemptation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlZorroTemptation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlZorroTemptationRepository extends JpaRepository<AlZorroTemptation, Long>, JpaSpecificationExecutor<AlZorroTemptation> {}
