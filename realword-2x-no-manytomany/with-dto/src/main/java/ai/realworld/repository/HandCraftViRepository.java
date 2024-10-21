package ai.realworld.repository;

import ai.realworld.domain.HandCraftVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HandCraftVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HandCraftViRepository extends JpaRepository<HandCraftVi, Long>, JpaSpecificationExecutor<HandCraftVi> {}
