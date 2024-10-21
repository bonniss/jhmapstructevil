package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CategoryMi;

/**
 * Spring Data JPA repository for the CategoryMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryMiRepository extends JpaRepository<CategoryMi, Long> {}
