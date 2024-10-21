package ai.realworld.repository;

import ai.realworld.domain.AlGore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlGore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlGoreRepository extends JpaRepository<AlGore, Long>, JpaSpecificationExecutor<AlGore> {}
