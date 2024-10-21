package ai.realworld.repository;

import ai.realworld.domain.HashRossVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HashRossVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HashRossViRepository extends JpaRepository<HashRossVi, Long>, JpaSpecificationExecutor<HashRossVi> {}
