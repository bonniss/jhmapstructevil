package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCategoryAlpha;

/**
 * Spring Data JPA repository for the NextCategoryAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCategoryAlphaRepository extends JpaRepository<NextCategoryAlpha, Long>, JpaSpecificationExecutor<NextCategoryAlpha> {}
