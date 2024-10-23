package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCategoryMi;

/**
 * Spring Data JPA repository for the NextCategoryMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCategoryMiRepository extends JpaRepository<NextCategoryMi, Long>, JpaSpecificationExecutor<NextCategoryMi> {}
