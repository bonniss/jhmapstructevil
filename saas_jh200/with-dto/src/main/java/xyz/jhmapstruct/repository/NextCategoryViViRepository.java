package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCategoryViVi;

/**
 * Spring Data JPA repository for the NextCategoryViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCategoryViViRepository extends JpaRepository<NextCategoryViVi, Long>, JpaSpecificationExecutor<NextCategoryViVi> {}
