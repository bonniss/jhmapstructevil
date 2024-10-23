package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCategoryVi;

/**
 * Spring Data JPA repository for the NextCategoryVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCategoryViRepository extends JpaRepository<NextCategoryVi, Long>, JpaSpecificationExecutor<NextCategoryVi> {}
