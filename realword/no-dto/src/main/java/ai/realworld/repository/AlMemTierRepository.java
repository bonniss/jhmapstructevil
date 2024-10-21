package ai.realworld.repository;

import ai.realworld.domain.AlMemTier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlMemTier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlMemTierRepository extends JpaRepository<AlMemTier, Long>, JpaSpecificationExecutor<AlMemTier> {}
