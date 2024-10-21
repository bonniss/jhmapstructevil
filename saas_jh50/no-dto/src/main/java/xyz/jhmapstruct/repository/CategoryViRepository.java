package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CategoryVi;

/**
 * Spring Data JPA repository for the CategoryVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryViRepository extends JpaRepository<CategoryVi, Long> {}
