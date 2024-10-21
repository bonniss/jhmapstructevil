package ai.realworld.repository;

import ai.realworld.domain.RihannaVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RihannaVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RihannaViRepository extends JpaRepository<RihannaVi, Long>, JpaSpecificationExecutor<RihannaVi> {}
