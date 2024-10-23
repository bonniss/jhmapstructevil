package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CategoryMiMi;

/**
 * Spring Data JPA repository for the CategoryMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryMiMiRepository extends JpaRepository<CategoryMiMi, Long>, JpaSpecificationExecutor<CategoryMiMi> {}
