package ai.realworld.repository;

import ai.realworld.domain.HashRoss;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HashRoss entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HashRossRepository extends JpaRepository<HashRoss, Long>, JpaSpecificationExecutor<HashRoss> {}
