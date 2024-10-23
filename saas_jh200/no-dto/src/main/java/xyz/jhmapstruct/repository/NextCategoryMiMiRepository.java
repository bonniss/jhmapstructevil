package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCategoryMiMi;

/**
 * Spring Data JPA repository for the NextCategoryMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCategoryMiMiRepository extends JpaRepository<NextCategoryMiMi, Long>, JpaSpecificationExecutor<NextCategoryMiMi> {}
