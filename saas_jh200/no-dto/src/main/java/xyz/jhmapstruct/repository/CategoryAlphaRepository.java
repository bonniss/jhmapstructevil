package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CategoryAlpha;

/**
 * Spring Data JPA repository for the CategoryAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryAlphaRepository extends JpaRepository<CategoryAlpha, Long>, JpaSpecificationExecutor<CategoryAlpha> {}
