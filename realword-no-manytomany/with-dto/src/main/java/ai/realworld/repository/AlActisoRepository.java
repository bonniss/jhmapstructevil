package ai.realworld.repository;

import ai.realworld.domain.AlActiso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlActiso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlActisoRepository extends JpaRepository<AlActiso, Long>, JpaSpecificationExecutor<AlActiso> {}
