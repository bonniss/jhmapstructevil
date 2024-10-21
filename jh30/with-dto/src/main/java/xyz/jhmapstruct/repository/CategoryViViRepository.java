package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CategoryViVi;

/**
 * Spring Data JPA repository for the CategoryViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryViViRepository extends JpaRepository<CategoryViVi, Long> {}
